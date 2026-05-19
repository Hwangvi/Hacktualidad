import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-landingHomeHero',
  templateUrl: './landingHomeHero.component.html',
  styleUrls: ['./landingHomeHero.component.css'],
  imports: [RouterLink]
})
export class LandingHomeHeroComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}
