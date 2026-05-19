import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ForumService } from '../../../core/service/forum.service';
import { Topic } from '../../../shared/interfaces/forum';

@Component({
  selector: 'app-tematicas',
  imports: [RouterLink, CommonModule],
  templateUrl: './affair.component.html',
  styleUrls: ['./affair.component.css'],
})
export class TematicasComponent implements OnInit {
  topics: Topic[] = [];

  constructor(private forumService: ForumService) {}

  ngOnInit() {
    this.forumService.getAllTopics().subscribe({
      next: (data) => {
        this.topics = data;
        console.log('Temáticas cargadas:', this.topics);
      },
      error: (err) => console.error('Error al cargar las temáticas:', err),
    });
  }
}
