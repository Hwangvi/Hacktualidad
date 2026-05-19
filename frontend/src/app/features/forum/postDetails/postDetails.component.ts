import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ForumService } from '../../../core/service/forum.service';
import { Post } from '../../../shared/interfaces/forum';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../../core/service/auth.service';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';
import { BANNED_WORDS } from '../../../shared/consts';

@Component({
  selector: 'app-post-details',
  templateUrl: './postDetails.component.html',
  styleUrls: ['./postDetails.component.css'],
  imports: [CommonModule, FormsModule, RouterLink],
})
export class PostDetailsComponent implements OnInit {
  post: Post | null = null;
  isLoading = true;
  error: string | null = null;
  newCommentContent: string = '';
  bannedWords = BANNED_WORDS



  constructor(
    private route: ActivatedRoute,
    private forumService: ForumService,
    public authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const postId = this.route.snapshot.paramMap.get('postId');
    if (postId) {
      const id = Number(postId);
      this.forumService.getPostById(id).subscribe({
        next: (data) => {
          if (!data.comments) {
            data.comments = [];
          }
          this.post = data;
          this.isLoading = false;
        },
        error: (err) => {
          this.error = 'No se pudo cargar el post.';
          this.isLoading = false;
          console.error(err);
        },
      });
    }
  }

  private containsBadWords(text: string): boolean {
    const regex = new RegExp(`\\b(${this.bannedWords.join('|')})\\b`, 'i');
    return regex.test(text);
  }

  addComment(): void {
    if (!this.newCommentContent.trim() || !this.post) {
      return;
    }

    if (this.containsBadWords(this.newCommentContent)) {
      Swal.fire({
        title: 'LENGUAJE NO PERMITIDO',
        text: 'El sistema ha detectado vocabulario soez o prohibido. Por favor, piensa dos veces antes de hacer un comentario.',
        icon: 'warning',
        background: '#0a0a0a',
        color: '#ff3333',
        confirmButtonColor: '#ff3333',
        confirmButtonText: '[ REFORMULAR ]',
        iconColor: '#ff3333',
        customClass: {
          popup: 'hacker-popup'
        }
      });
      return;
    }

    this.forumService
      .addCommentToPost(this.post.postId, { content: this.newCommentContent })
      .subscribe({
        next: (newComment) => {
          this.post?.comments.push(newComment);
          this.newCommentContent = '';

          const Toast = Swal.mixin({
            toast: true,
            position: 'top-end',
            showConfirmButton: false,
            timer: 3000,
            background: '#0a0a0a',
            color: '#00ff00'
          });
          Toast.fire({ icon: 'success', title: 'Transmisión enviada' });
        },
        error: (err) => {
          console.error('Error al añadir el comentario:', err);
          Swal.fire({
            title: 'ERROR DE TRANSMISIÓN',
            text: 'Hubo un error al enviar tu comentario.',
            icon: 'error',
            background: '#0a0a0a',
            color: '#ff3333'
          });
        },
      });
  }

  deletePost(): void {
    if (!this.post) return;

    Swal.fire({
      title: '¿BORRAR DATOS?',
      text: 'No podrás recuperar este post una vez eliminado.',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'SÍ, BORRAR',
      cancelButtonText: 'CANCELAR',
      background: '#0a0a0a',
      color: '#ff3333',
      confirmButtonColor: '#ff3333',
      cancelButtonColor: '#333'
    }).then((result) => {
      if (result.isConfirmed) {
        this.forumService.deletePost(this.post!.postId).subscribe({
          next: () => {
            Swal.fire({
              title: 'ELIMINADO',
              text: 'El post ha sido purgado.',
              icon: 'success',
              background: '#0a0a0a',
              color: '#00ff00',
              confirmButtonColor: '#00ff00'
            });
            this.router.navigate(['/forum']);
          },
          error: (err) => {
            Swal.fire('Error', 'No se pudo borrar el post.', 'error');
            console.error(err);
          },
        });
      }
    });
  }
}
