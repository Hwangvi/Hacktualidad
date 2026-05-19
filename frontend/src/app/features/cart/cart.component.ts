import { Cart } from './../../shared/interfaces/Cart';
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { CartService } from '../../core/service/cart.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css'],
})
export class CartComponent implements OnInit {
  cart: Cart | null = null;

  constructor(private cartService: CartService) {}

  ngOnInit(): void {
    this.cartService.cart$.subscribe((data) => {
      this.cart = data;
    });

    this.cartService.getCart().subscribe({
      error: (err) => console.log('Usuario no logueado'),
    });
  }

  removeItem(productId: number): void {
    Swal.fire({
      title: '¿ELIMINAR PRODUCTO?',
      text: 'Eliminarás este producto de tu carrito.',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'ELIMINAR PRODUCTO ',
      cancelButtonText: 'CANCELAR',
      background: 'rgba(10, 20, 10, 0.95)',
      color: '#00ff00',
      iconColor: '#ff0055',
      confirmButtonColor: '#ff0055',
      cancelButtonColor: '#00ff00',
      reverseButtons: true,
      backdrop: `rgba(0,0,0,0.8)`,
    }).then((result) => {
      if (result.isConfirmed) {
        this.cartService.removeFromCart(productId).subscribe(() => {
          Swal.fire({
            title: 'PRODUCTO ELIMINADO DEL CARRITO',
            text: 'Producto eliminado correctamente.',
            icon: 'success',
            background: 'rgba(10, 20, 10, 0.95)',
            color: '#00ff00',
            confirmButtonColor: '#00ff00',
            iconColor: '#00ff00',
          });
        });
      }
    });
  }

  buy(): void {
    if (!this.cart || this.cart.items.length === 0) return;

    Swal.fire({
      title: 'PROCESANDO PEDIDO...',
      text: 'El pedido se esta procesando. Por favor, espere...',
      icon: 'info',
      showConfirmButton: false,
      allowOutsideClick: false,
      background: 'rgba(10, 20, 10, 0.95)',
      color: '#00ff00',
      didOpen: () => {
        Swal.showLoading();
        this.cartService.checkout().subscribe({
          next: () => {
            Swal.fire({
              title: 'PEDIDO TRAMITADO CON EXITO',
              text: 'Puedes comprobar el recibo del pedido en tu correo electrónico',
              icon: 'success',
              background: 'rgba(10, 20, 10, 0.95)',
              color: '#00ff00',
              confirmButtonColor: '#00ff00',
              iconColor: '#00ff00',
            });
          },
          error: (err) => {
            console.error(err);
            Swal.fire({
              title: 'ERROR',
              text: 'No se ha podido realizar el pedido. Contacta con el servicio de atención al cliente',
              icon: 'error',
              background: 'rgba(10, 20, 10, 0.95)',
              color: '#ff3333',
              confirmButtonColor: '#ff3333',
            });
          },
        });
      },
    });
  }
}
