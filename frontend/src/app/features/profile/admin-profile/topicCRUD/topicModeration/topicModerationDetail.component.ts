import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import Swal from 'sweetalert2';
import { ForumService } from '../../../../../core/service/forum.service';
import { Topic } from '../../../../../shared/interfaces/forum';

@Component({
  selector: 'app-topic-moderation-detail',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './topicModerationDetail.component.html',
  styleUrls: ['./topicModerationDetail.component.css']
})
export class TopicModerationDetailComponent implements OnInit {
  topic: Topic | null = null;
  isLoading = true;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private forumService: ForumService
  ) { }

  ngOnInit(): void {
    const topicId = this.route.snapshot.paramMap.get('id');
    if (topicId) {
      this.loadTopicDetails(+topicId);
    } else {
      this.isLoading = false;
    }
  }

  loadTopicDetails(id: number): void {
    this.forumService.getAllTopics().subscribe({
      next: (topics) => {
        this.topic = topics.find(t => t.topicId === id) || null;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error al cargar la temática', err);
        this.isLoading = false;
      }
    });
  }

  deleteThisTopic(): void {
    if (!this.topic) return;

    Swal.fire({
      title: '¿Estás completamente seguro?',
      html: `Vas a borrar la temática "<b>${this.topic.topicName}</b>".<br>Todos sus posts serán eliminados. Esta acción es irreversible.`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Sí, ¡bórrala!',
      cancelButtonText: 'Cancelar',
      confirmButtonColor: '#d33',
      cancelButtonColor: '#3085d6',
    }).then((result) => {
      if (result.isConfirmed) {
        this.forumService.deleteTopic(this.topic!.topicId).subscribe({
          next: () => {
            Swal.fire('¡Eliminada!', 'La temática ha sido borrada con éxito.', 'success');
            this.router.navigate(['/profile/admin/forum/topics/moderate']);
          },
          error: (err) => {
            console.error('Error al borrar la temática', err);
            Swal.fire('Error', 'No se pudo borrar la temática.', 'error');
          }
        });
      }
    });
  }
}
