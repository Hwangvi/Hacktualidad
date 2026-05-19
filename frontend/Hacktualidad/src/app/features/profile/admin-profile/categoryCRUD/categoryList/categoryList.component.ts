import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { Observable, Subject } from 'rxjs';
import { startWith, switchMap } from 'rxjs/operators';
import Swal from 'sweetalert2';
import { Category } from '../../../../../shared/interfaces/Category';
import { CategoryService } from '../../../../../core/service/Category.service';
import { CategoryColorPipe } from '../../../../../shared/pipes/categoryColor.pipe';

@Component({
  selector: 'app-category-list',
  standalone: true,
  imports: [CommonModule, RouterModule, CategoryColorPipe],
  templateUrl: './categoryList.component.html',
  styleUrls: ['./categoryList.component.css'],
})
export class CategoryListComponent implements OnInit {
  private refreshTrigger$ = new Subject<void>();
  categories$!: Observable<Category[]>;

  constructor(private categoryService: CategoryService, private router: Router) {}

  ngOnInit(): void {
    this.categories$ = this.refreshTrigger$.pipe(
      startWith(undefined),
      switchMap(() => this.categoryService.getCategories())
    );
  }

  loadCategories(): void {
    this.refreshTrigger$.next();
  }

  onDeleteCategory(id: number): void {
    Swal.fire({
      title: '¿Estás seguro?',
      text: 'Esta acción no se puede revertir. ¿Desea borrar esta categoría?',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Sí, ¡bórralo!',
      cancelButtonText: 'Cancelar',
      background: '#141414',
      color: '#ff3333',
      iconColor: '#ff3333',
      confirmButtonColor: '#ff3333',
      cancelButtonColor: '#333',
    }).then((result) => {
      if (result.isConfirmed) {
        this.categoryService.deleteCategory(id).subscribe({
          next: () => {
            Swal.fire('¡Eliminada!', 'La categoría ha sido borrada.', 'success');
            this.loadCategories();
          },
          error: () => Swal.fire('Error', 'No se pudo borrar la categoría.', 'error'),
        });
      }
    });
  }

  onCancel(): void {
    this.router.navigate(['/profile/admin']);
  }
}
