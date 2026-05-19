import { Component, OnInit, OnDestroy } from '@angular/core';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Subscription } from 'rxjs';
import { AuthService } from '../../../core/service/auth.service';
import { User } from '../../../shared/interfaces/User';

@Component({
  selector: 'app-odds',
  templateUrl: './odds.component.html',
  styleUrls: ['./odds.component.css'],
  imports: [CommonModule, RouterLink],
})
export class OddsComponent implements OnInit, OnDestroy {
  isLoggedIn: boolean = false;
  profileLink: string = '/login';
  private userSubscription!: Subscription;

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    this.userSubscription = this.authService.currentUser$.subscribe((user: User | null) => {
      if (user) {
        this.isLoggedIn = true;
        if (user.role === 'ADMIN') {
          this.profileLink = '/profile/admin';
        } else if (user.role === 'USER') {
          this.profileLink = '/profile/user';
        }
      } else {
        this.isLoggedIn = false;
        this.profileLink = '/login';
      }
    });
  }

  ngOnDestroy(): void {
    if (this.userSubscription) {
      this.userSubscription.unsubscribe();
    }
  }
}
