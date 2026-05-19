import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { Cart } from '../../shared/interfaces/Cart';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class CartService {
  private apiUrl = `${environment.apiUrl}/cart`;

  private cartSubject = new BehaviorSubject<Cart | null>(null);
  public cart$ = this.cartSubject.asObservable();

  constructor(private http: HttpClient) {
    this.getCart().subscribe({
      error: () => console.log('Usuario no autenticado o carrito vacío'),
    });
  }

  getCart(): Observable<Cart> {
  const urlNoCache = `${this.apiUrl}?t=${new Date().getTime()}`;

  return this.http
    .get<Cart>(urlNoCache, { withCredentials: true })
    .pipe(tap((cart) => this.cartSubject.next(cart)));
}

  addToCart(productId: number): Observable<Cart> {
    const url = `${this.apiUrl}/add/${productId}`;
    return this.http.post<Cart>(url, {}, { withCredentials: true }).pipe(
      tap((updatedCart) => {
        this.cartSubject.next(updatedCart);
      })
    );
  }

  removeFromCart(productId: number): Observable<Cart> {
    const url = `${this.apiUrl}/remove/${productId}`;
    return this.http.delete<Cart>(url, { withCredentials: true }).pipe(
      tap((updatedCart) => {
        this.cartSubject.next(updatedCart);
      })
    );
  }

  getCurrentCartValue(): Cart | null {
    return this.cartSubject.value;
  }

  checkout(): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/checkout`, {}, { withCredentials: true }).pipe(
      tap(() => {
        this.cartSubject.next(null);
      })
    );
  }
}
