import { Component, OnInit } from '@angular/core';
import { HeroComponent } from './hero/hero.component';
import { OddsComponent } from './odds/odds.component';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
  imports: [HeroComponent, OddsComponent],
})
export class HomeComponent implements OnInit {
  constructor() {}

  ngOnInit() {}
}
