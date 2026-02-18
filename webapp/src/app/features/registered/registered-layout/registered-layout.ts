import {Component, OnDestroy, OnInit} from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {PassengerHeaderComponent} from '../components/passenger-header.component';
import {WebSocketService} from '../../../core/services/websocket.service';
import {CurrentUserService} from '../../../core/services/current-user.service';
import {Subscription} from 'rxjs';


@Component({
  selector: 'app-registered-layout',
  standalone: true,
  imports: [RouterOutlet, PassengerHeaderComponent],
  template: `<div class="min-h-screen bg-app-dark">
        <app-passenger-header></app-passenger-header>

        <div class="flex h-[calc(100vh-94px)]">
            <aside class="w-106 bg-app-dark text-white">
              <!-- <app-registered-sidebar></app-registered-sidebar> -->
              <router-outlet name="aside"></router-outlet>
            </aside>

            <main class="flex-1 bg-white overflow-auto">
            <router-outlet></router-outlet>
            </main>
        </div>
        </div>
        `,
})
export class RegisteredLayout implements OnInit, OnDestroy {
  private userSubscription?: Subscription;

  constructor(
    private currentUserService: CurrentUserService,
    private websocketService: WebSocketService
  ) {}

  ngOnInit(): void {
    this.userSubscription = this.currentUserService.currentUser$.subscribe({
      next: (user) => {
        if (user?.id && user.role === 'PASSENGER' && !this.websocketService.isConnected()) {
          this.websocketService.connect(user.id);
        }
      }
    });
  }

  ngOnDestroy(): void {
    this.userSubscription?.unsubscribe();
    this.websocketService.disconnect();
  }
}
