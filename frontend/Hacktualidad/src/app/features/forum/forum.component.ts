import { Component, OnInit } from '@angular/core';
import { TematicasComponent } from './affair/affair.component';

@Component({
  selector: 'app-forum',
  templateUrl: './forum.component.html',
  styleUrls: ['./forum.component.css'],
  imports: [TematicasComponent]

})
export class ForumComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}
