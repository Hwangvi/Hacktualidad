import { ProductService } from './../../../../core/service/product.service';
import { Component, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Product } from '../../../../shared/interfaces/Product';
import Swal from 'sweetalert2';
import { Observable, Subject } from 'rxjs';
import { startWith, switchMap } from 'rxjs/operators';

@Component({
  selector: 'app-product-list',
  imports: [CommonModule, RouterModule],
  templateUrl: './productList.component.html',
  styleUrls: ['./productList.component.css']
})
export class ProductListComponent implements OnInit {

  private refreshTrigger$ = new Subject<void>();

  products$!: Observable<Product[]>;

  constructor(private productService: ProductService, private router: Router) {}

  ngOnInit(): void {
    this.products$ = this.refreshTrigger$.pipe(
      startWith(undefined),
      switchMap(() => this.productService.getProducts())
    );
  }

  loadProducts(): void {
    this.refreshTrigger$.next();
  }

  onDeleteProduct(id: number): void {
  Swal.fire({
    title: '¿EJECUTAR BORRADO?',
    text: "Esta acción eliminará el producto permanentemente. ¿Proceder?",
    icon: 'warning',
    showCancelButton: true,
    confirmButtonText: 'SÍ, BORRAR',
    cancelButtonText: 'CANCELAR',
    background: '#141414',
    color: '#ff3333',
    iconColor: '#ff3333',
    confirmButtonColor: '#ff3333',
    cancelButtonColor: '#333',
    reverseButtons: true
  }).then((result) => {
    if (result.isConfirmed) {
      this.productService.deleteProduct(id).subscribe({
        next: () => {
          Swal.fire({
            title: '¡ELIMINADO!',
            text: 'El archivo ha sido purgado del sistema.',
            icon: 'success',
            background: '#141414',
            color: '#00ffcc',
            confirmButtonColor: '#00ffcc',
            iconColor: '#00ffcc'
          });
          this.loadProducts();
        },
        error: (err) => {
          console.error('Error al borrar:', err);
          Swal.fire({
            title: 'ERROR DE ACCESO',
            text: 'No se pudo eliminar el producto.',
            icon: 'error',
            background: '#141414',
            color: '#ff3333',
            confirmButtonColor: '#ff3333'
          });
        }
      });
    }
  });
  }

  onCancel(): void {
    this.router.navigate(['/profile/admin']);
  }
}
