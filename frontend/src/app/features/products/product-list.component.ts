import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProductService } from '../../core/services/product.service';
import { Product, PagedProductsResponse } from '../../core/models/product.model';

@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.scss'],
})
export class ProductListComponent implements OnInit {
  products: Product[] = [];
  currentPage = 0;
  pageSize = 10;
  totalElements = 0;
  totalPages = 0;
  loading = false;
  errorMessage: string | null = null;

  constructor(private productService: ProductService) {}

  ngOnInit(): void {
    this.loadProducts();
  }

  loadProducts(): void {
    this.loading = true;
    this.errorMessage = null;

    this.productService.getProducts(this.currentPage, this.pageSize).subscribe({
      next: (response: PagedProductsResponse) => {
        this.products = response.items;
        this.currentPage = response.page;
        this.totalElements = response.totalElements;
        this.totalPages = response.totalPages;
        this.loading = false;
      },
      error: (err: any) => {
        this.loading = false;
        this.errorMessage = 'Ürünler yüklenirken bir hata oluştu.';
        console.error('Error loading products:', err);
      },
    });
  }

  goToPage(page: number): void {
    if (page >= 0 && page < this.totalPages) {
      this.currentPage = page;
      this.loadProducts();
    }
  }

  previousPage(): void {
    this.goToPage(this.currentPage - 1);
  }

  nextPage(): void {
    this.goToPage(this.currentPage + 1);
  }

  get pages(): number[] {
    const pages: number[] = [];
    const start = Math.max(0, this.currentPage - 2);
    const end = Math.min(this.totalPages, start + 5);
    for (let i = start; i < end; i++) {
      pages.push(i);
    }
    return pages;
  }
}
