import { Component, OnInit } from '@angular/core';
import { LandingHomeHeroComponent } from "./landingHomeHero/landingHomeHero.component";
import { landingOddsComponent } from "./landingOdds/landingOdds.component";

@Component({
  selector: 'app-landingHome',
  templateUrl: './landingHome.component.html',
  styleUrls: ['./landingHome.component.css'],
  imports: [LandingHomeHeroComponent, landingOddsComponent]
})
export class LandingHomeComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}
