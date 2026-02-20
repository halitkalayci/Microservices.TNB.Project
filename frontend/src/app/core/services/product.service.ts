import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { PagedProductsResponse } from '../models/product.model';

@Injectable({
  providedIn: 'root',
})
export class ProductService {
  private readonly baseUrl = `${environment.bffUrl}/api/v1/products`;

  constructor(private http: HttpClient) {}

  getProducts(page: number = 0, size: number = 10): Observable<PagedProductsResponse> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<PagedProductsResponse>(this.baseUrl, { params });
  }

  getProductById(id: string): Observable<any> {
    return this.http.get(`${this.baseUrl}/${id}`);
  }
}
