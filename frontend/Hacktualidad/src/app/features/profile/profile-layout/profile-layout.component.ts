import { Component, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-profile-layout',
  templateUrl: './profile-layout.component.html',
  styleUrls: ['./profile-layout.component.css'],
  imports: [RouterOutlet]
})
export class ProfileLayoutComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}
