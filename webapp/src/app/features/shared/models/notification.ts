export interface AppNotification {
  id: number;
  type: 'RIDE_ACCEPTED' | 'RIDE_COMPLETED';
  message: string;
  rideId: number | null;
  read: boolean;
  createdAt: string;
}
