import { AuthService } from './../../../core/service/auth.service';
import { CommonModule } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { RouterLink, Router, RouterLinkActive } from "@angular/router";
import { CartService } from '../../../core/service/cart.service';
import { Subscription } from 'rxjs';
import { User } from '../../interfaces/User';
import { Cart } from '../../interfaces/Cart';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'],
  imports: [RouterLink, CommonModule, RouterLinkActive]
})
export class HeaderComponent implements OnInit, OnDestroy {

  isLoggedIn: boolean = false;
  profileLink: string = '/login';
  cartItemCount: number = 0;

  isMobileMenuOpen: boolean = false;

  private userSubscription!: Subscription;
  private cartSubscription!: Subscription;

  constructor(
    private authService: AuthService,
    private router: Router,
    private cartService: CartService
  ) { }

  ngOnInit(): void {
    this.userSubscription = this.authService.currentUser$.subscribe((user: User | null) => {
      if (user) {
        this.isLoggedIn = true;

        this.cartService.getCart().subscribe();

        if (user.role === 'ADMIN') {
          this.profileLink = '/profile/admin';
        } else if (user.role === 'USER') {
          this.profileLink = '/profile/user';
        }
      } else {
        this.isLoggedIn = false;
        this.profileLink = '/login';
        this.cartItemCount = 0;
      }
    });

    this.cartSubscription = this.cartService.cart$.subscribe((cart: Cart | null) => {
      if (cart && cart.items) {
        this.cartItemCount = cart.items.reduce((acc, item) => acc + item.quantity, 0);
      } else {
        this.cartItemCount = 0;
      }
    });
  }

  toggleMenu(): void {
    this.isMobileMenuOpen = !this.isMobileMenuOpen;
  }

  closeMenu(): void {
    this.isMobileMenuOpen = false;
  }

  logout(): void {
    this.closeMenu();
    this.authService.logout();
    this.cartItemCount = 0;
    this.router.navigate(['/login']);
  }

  ngOnDestroy(): void {
    if (this.userSubscription) {
      this.userSubscription.unsubscribe();
    }
    if (this.cartSubscription) {
      this.cartSubscription.unsubscribe();
    }
  }
}
