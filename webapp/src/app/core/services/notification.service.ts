import { HttpClient } from '@angular/common/http';
import { effect, Injectable } from '@angular/core';
import { CurrentRideStateService } from '../../features/registered/services/current-ride-state.service';
import { ConfigService } from './config.service';
import { CurrentUserService } from './current-user.service';
import {BehaviorSubject} from 'rxjs';
import {AppNotification} from '../../features/shared/models/notification';

@Injectable({
  providedIn: 'root',
})
export class NotificationService {
  private notificationsSubject = new BehaviorSubject<AppNotification[]>([]);
  public notifications$ = this.notificationsSubject.asObservable();

  private unreadCountSubject = new BehaviorSubject<number>(0);
  public unreadCount$ = this.unreadCountSubject.asObservable();
  constructor(
    private rideState: CurrentRideStateService,
    private http: HttpClient,
    private configService: ConfigService,
  ) {
    effect(() => {
      const panicActive = this.rideState.panicSignal().pressed;
      if (panicActive) {
        this.notifyAdminAboutPanic(this.rideState.panicSignal().rideId, this.rideState.panicSignal().userId);
      }
    });
  }
  notifyAdminAboutPanic(rideId: number, userId: number | null) : void {
    this.http.post(this.configService.ridesUrl + `/${rideId}/panic`, { userId }).subscribe({
      next: () => {},
      error: (err) => {
        console.error('Failed to notify admin about panic', err);
      }
    });
  }
  loadNotifications(): void {
    this.http.get<AppNotification[]>(this.configService.notificationUrl).subscribe({
      next: (notifications) => {
        this.notificationsSubject.next(notifications);
        this.updateUnreadCount();
      },
      error: (err) => console.error('Failed to load notifications', err),
    });
  }

  loadUnreadCount(): void {
    this.http.get<number>(`${this.configService.notificationUrl}/unread-count`).subscribe({
      next: (count) => this.unreadCountSubject.next(count),
      error: (err) => console.error('Failed to load unread count', err),
    });
  }

  markAsRead(notificationId: number): void {
    this.http
      .put<void>(`${this.configService.notificationUrl}/${notificationId}/read`, {})
      .subscribe({
        next: () => {
          const notifications = this.notificationsSubject.value.map((n) =>
            n.id === notificationId ? { ...n, read: true } : n
          );
          this.notificationsSubject.next(notifications);
          this.updateUnreadCount();
        },
        error: (err) => console.error('Failed to mark notification as read', err),
      });
  }

  addNotification(notification: AppNotification): void {
    const current = this.notificationsSubject.value;
    this.notificationsSubject.next([notification, ...current]);
    this.updateUnreadCount();
  }

  private updateUnreadCount(): void {
    const count = this.notificationsSubject.value.filter((n) => !n.read).length;
    this.unreadCountSubject.next(count);
  }
}



