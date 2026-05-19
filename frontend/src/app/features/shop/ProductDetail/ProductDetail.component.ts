import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { ProductService } from '../../../core/service/product.service';
import { CartService } from '../../../core/service/cart.service';
import { Product } from '../../../shared/interfaces/Product';
import Swal from 'sweetalert2'

@Component({
  selector: 'app-product-detail',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './ProductDetail.component.html',
  styleUrls: ['./ProductDetail.component.css']
})
export class ProductDetailComponent implements OnInit {
  product: Product | null = null;
  loading: boolean = true;
  processing: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private productService: ProductService,
    private cartService: CartService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (id) {
      this.productService.getProductById(id).subscribe({
        next: (data) => {
          this.product = data;
          this.loading = false;
        },
        error: (err) => {
          console.error(err);
          this.loading = false;
        }
      });
    }
  }

  addToCart(): void {
    if (!this.product) return;
    Swal.fire({
      title: 'AÑADIR AL CARRITO',
      text: `¿Quieres añadir: "${this.product.name}" al carrito?`,
      icon: 'question',
      showCancelButton: true,
      confirmButtonText: 'AÑADIR',
      cancelButtonText: 'CANCELAR',
      background: '#141414',
      color: '#0f0',
      iconColor: '#0f0)',
      confirmButtonColor: 'rgba(0, 255, 0, 0.5)',
      cancelButtonColor: '#ff3333',
      reverseButtons: true,
      backdrop: `
        rgba(0,0,0,0.8)
        url("/assets/images/grid-overlay.png")
        left top
        no-repeat
      `
    }).then((result) => {
      if (result.isConfirmed) {

        this.processing = true;

        this.cartService.addToCart(this.product!.productId).subscribe({
          next: (cart) => {
            this.processing = false;

            Swal.fire({
              title: '¡EXITO!',
              text: 'El producto ha sido añadido a tu carrito',
              icon: 'success',
              background: '#141414',
              color: '#0f0',
              confirmButtonColor: '#0f0',
              iconColor: '#0f0'
            });
          },
          error: (err) => {
            this.processing = false;
            if (err.status === 403 || err.status === 401) {
               Swal.fire({
                title: 'ACCESO DENEGADO',
                text: 'NECESITAS LOGEARTE PARA REALIZAR ESTA ACCION',
                icon: 'error',
                background: '#141414',
                color: '#ff3333',
                confirmButtonColor: '#ff3333'
               }).then(() => this.router.navigate(['/login']));
            } else {
               Swal.fire({
                title: 'ERROR DE CONEXION',
                text: 'No se ha podido establecer la conexion con el servidor.',
                icon: 'error',
                background: '#141414',
                color: '#ff3333'
               });
            }
          }
        });
      }
    });
  }
}
