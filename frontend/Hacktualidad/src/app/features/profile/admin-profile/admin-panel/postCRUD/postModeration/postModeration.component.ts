import { Component, OnInit } from '@angular/core';
import { ForumService } from '../../../../../../core/service/forum.service';
import { Post } from '../../../../../../shared/interfaces/forum';
import { CommonModule, Location } from '@angular/common';
import { RouterLink, ActivatedRoute } from '@angular/router';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-post-moderation',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './postModeration.component.html',
  styleUrls: ['./postModeration.component.css']
})
export class PostModerationComponent implements OnInit {
  posts: Post[] = [];
  isLoading = true;
  topicName: string | null = null;

   constructor(
    private forumService: ForumService,
    private route: ActivatedRoute,
    private location: Location
  ) { }

  ngOnInit(): void {
    this.topicName = this.route.snapshot.paramMap.get('topicName');
    if (this.topicName) {
      this.loadPostsByTopic(this.topicName);
    } else {

      this.isLoading = false;
    }
  }

  loadPostsByTopic(topicName: string): void {
    this.isLoading = true;
    this.forumService.getPostsByTopic(topicName).subscribe({
      next: (data) => {
        this.posts = data;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error al cargar los posts', err);
        this.isLoading = false;
        Swal.fire('Error', 'No se pudieron cargar los posts.', 'error');
      }
    });
  }

  goBack(): void {
    this.location.back();
  }

  deletePost(postId: number): void {
    Swal.fire({
      title: '¿Estás seguro?',
      text: "Esta acción no se puede revertir.",
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Sí, borrar',
      cancelButtonText: 'Cancelar',
      background: '#19191a',
      color: '#0a9400ff'
    }).then((result) => {
      if (result.isConfirmed) {
        this.forumService.deletePost(postId).subscribe({
          next: () => {
            this.posts = this.posts.filter(p => p.postId !== postId);
            Swal.fire({
              title: 'Eliminado',
              text: 'El post ha sido borrado.',
              icon: 'success',
              background: '#19191a',
              color: '#ffffff'
            });
          },
          error: (err) => {
            console.error('Error al borrar el post', err);
            ({
              title: 'Error',
              text: 'No se pudo borrar el post.',
              icon: 'error',
              background: '#19191a',
              color: '#ffffff'
            });
          }
        });
      }
    });
  }
}
