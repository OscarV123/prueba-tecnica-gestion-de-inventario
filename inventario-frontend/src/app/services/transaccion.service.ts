import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Transaccion } from './transaccion';

@Injectable({
  providedIn: 'root',
})
export class TransaccionService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/transacciones';

  getTransacciones(): Observable<Transaccion[]> {
    return this.http.get<Transaccion[]>(this.apiUrl);
  }

  postTransaccion(transaccion: Transaccion): Observable<Transaccion> {
    return this.http.post<Transaccion>(this.apiUrl, transaccion);
  }

  getTransaccionesByProductoId(productoId: number): Observable<Transaccion[]> {
    return this.http.get<Transaccion[]>(`${this.apiUrl}/producto/${productoId}`);
  }
}
