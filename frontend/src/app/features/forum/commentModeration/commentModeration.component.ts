import { Post, Comment } from '../../../shared/interfaces/forum';
import { Component, OnInit } from '@angular/core';
import { CommonModule, Location } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { ForumService } from '../../../core/service/forum.service';
import Swal from 'sweetalert2';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-comment-moderation',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './commentModeration.component.html',
  styleUrls: ['./commentModeration.component.css'],
})
export class CommentModerationComponent implements OnInit {
  post: Post | null = null;
  comments: any[] = [];
  isLoading = true;
  selectedCommentIds: Set<number> = new Set();

  constructor(
    private forumService: ForumService,
    private route: ActivatedRoute,
    private location: Location
  ) {}

  ngOnInit(): void {
    const postId = this.route.snapshot.paramMap.get('postId');
    if (postId) {
      this.loadPostAndComments(Number(postId));
    } else {
      this.isLoading = false;
    }
  }

  loadPostAndComments(postId: number): void {
    this.isLoading = true;
    this.forumService.getPostById(postId).subscribe({
      next: (data: Post) => {
        this.post = data;
        this.comments = data.comments || [];

        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error al cargar el post', err);
        this.isLoading = false;
        Swal.fire('Error', 'No se pudieron cargar los datos.', 'error');
      },
    });
  }

  toggleSelection(commentId: number, event: any): void {
    if (event.target.checked) {
      this.selectedCommentIds.add(commentId);
    } else {
      this.selectedCommentIds.delete(commentId);
    }
  }

  toggleAll(event: any): void {
    if (event.target.checked) {
      this.comments.forEach((c) => this.selectedCommentIds.add(c.commentId));
    } else {
      this.selectedCommentIds.clear();
    }
  }

  isSelected(commentId: number): boolean {
    return this.selectedCommentIds.has(commentId);
  }

  areAllSelected(): boolean {
    return this.comments.length > 0 && this.selectedCommentIds.size === this.comments.length;
  }

  deleteSelectedComments(): void {
    const count = this.selectedCommentIds.size;
    if (count === 0) {
      Swal.fire({
      title: `¡Error!`,
      text: 'Selecciona al menos un comentario.',
      icon: 'warning',
      background: '#0a190a',
      color: '#00ff00',});
      return;
    }

    Swal.fire({
      title: count == 1 ? `¿Borrar ${count} comentario?` : `¿Borrar ${count} comentarios?`,
      text: 'No podrás revertir esto.',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Borrar comentario',
      cancelButtonText: 'Cancelar',
      confirmButtonColor: '#ff0055',
      background: '#0a190a',
      color: '#00ff00',
    }).then((result) => {
      if (result.isConfirmed) {
        this.performBulkDelete();
      }
    });
  }

  private performBulkDelete(): void {
    const deleteRequests = Array.from(this.selectedCommentIds).map((id) =>
      this.forumService.deleteComment(id)
    );

    forkJoin(deleteRequests).subscribe({
      next: () => {
        Swal.fire({
          title: 'SISTEMA LIMPIO',
          text: 'Comentarios eliminados del sistema.',
          icon: 'success',
          background: '#0a190a',
          color: '#00ff00',
          iconColor: '#00ff00',
          confirmButtonColor: '#00ff00',
          confirmButtonText: 'CONFIRMAR',
          timer: 2000,
        });

        this.comments = this.comments.filter((c) => !this.selectedCommentIds.has(c.commentId));
        this.selectedCommentIds.clear();
      },
      error: (err) => {
        console.error('Error en borrado masivo', err);

        Swal.fire({
          title: 'ERROR CRÍTICO',
          text: 'Fallo al intentar borrar algunos comentarios.',
          icon: 'error',
          background: '#0a190a',
          color: '#ff0055',
          iconColor: '#ff0055',
          confirmButtonColor: '#ff0055',
          confirmButtonText: 'CERRAR',
        });

        if (this.post) this.loadPostAndComments(this.post.postId);
      },
    });
  }

  goBack(): void {
    this.location.back();
  }
}
