import { Component, OnInit } from '@angular/core';
import { ProductListComponent } from './product-list/product-list.component';

@Component({
  selector: 'app-shop',
  templateUrl: './shop.component.html',
  styleUrls: ['./shop.component.css'],
  imports: [ProductListComponent]
})
export class ShopComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}
