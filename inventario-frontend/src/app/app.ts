import { Component, signal, OnInit, AfterViewInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ProductoService } from './services/producto.service';
import { ProveedorService } from './services/proveedor.service';
import { CategoriaService } from './services/categoria.service';
import { TransaccionService } from './services/transaccion.service';

interface EndpointResult {
  status: number | null;
  ok: boolean;
  body: string;
  ms: number;
  url: string;
  method: string;
  visible: boolean;
}

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App implements OnInit, AfterViewInit {
  protected readonly title = signal('inventario-frontend');

  // ── Servicios (se conservan por si los necesitas en el futuro) ──
  private prodService = inject(ProductoService);
  private provService = inject(ProveedorService);
  private catService = inject(CategoriaService);
  private transService = inject(TransaccionService);
  private cdr = inject(ChangeDetectorRef);

  // ── API Tester state ──
  baseUrl = 'http://localhost:8080';
  open: Record<string, boolean> = {};
  results: Record<string, EndpointResult | null> = {};
  loading: Record<string, boolean> = {};

  // ── Proveedor fields ──
  getProveedorById_id = '';
  pp_nombreEmpresa = '';
  pp_ruc = '';
  pp_nombreContacto = '';
  pp_telefono = '';
  pp_email = '';
  putProv_id = '';
  putProv_nombreEmpresa = '';
  putProv_ruc = '';
  putProv_nombreContacto = '';
  putProv_telefono = '';
  putProv_email = '';
  delProv_id = '';

  // ── Producto fields ──
  getProdById_id = '';
  cp_nombre = '';
  cp_descripcion = '';
  cp_precio: number | null = null;
  cp_stock: number | null = null;
  cp_categoriaId: number | null = null;
  cp_proveedorId: number | null = null;
  up_id = '';
  up_nombre = '';
  up_descripcion = '';
  up_precio: number | null = null;
  up_stock: number | null = null;
  up_categoriaId: number | null = null;
  up_proveedorId: number | null = null;
  dp_id = '';
  sb_limite: number | null = null;

  // ── Categoria fields ──
  getCatById_id = '';
  cc_nombre = '';
  cc_descripcion = '';
  uc_id = '';
  uc_nombre = '';
  uc_descripcion = '';
  dc_id = '';

  // ── Transaccion fields ──
  ct_productoId: number | null = null;
  ct_tipo = '';
  ct_cantidad: number | null = null;
  ct_motivo = '';
  gt_productoId = '';

  ngOnInit() {}

  ngAfterViewInit() {
    const sections = document.querySelectorAll('section.controller');
    const links = document.querySelectorAll('aside a');
    const observer = new IntersectionObserver(
      (entries) => {
        entries.forEach((e) => {
          if (e.isIntersecting) {
            links.forEach((l) => l.classList.remove('active'));
            const active = document.querySelector(
              `aside a[href="#${(e.target as HTMLElement).id}"]`,
            );
            if (active) active.classList.add('active');
          }
        });
      },
      { threshold: 0.3 },
    );
    sections.forEach((s) => observer.observe(s));
  }

  toggle(id: string) {
    this.open[id] = !this.open[id];
  }
  isOpen(id: string): boolean {
    return !!this.open[id];
  }
  getResult(id: string) {
    return this.results[id] ?? null;
  }
  isLoading(id: string): boolean {
    return !!this.loading[id];
  }

  private highlight(json: string): string {
    return json
      .replace(/&/g, '&amp;')
      .replace(/</g, '&lt;')
      .replace(/>/g, '&gt;')
      .replace(
        /("(\\u[a-zA-Z0-9]{4}|\\[^u]|[^\\"])*"(\s*:)?|\b(true|false|null)\b|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?)/g,
        (m: string) => {
          if (/^"/.test(m))
            return /:$/.test(m)
              ? `<span style="color:#7dd3fc">${m}</span>`
              : `<span style="color:#86efac">${m}</span>`;
          if (/true|false/.test(m)) return `<span style="color:#f472b6">${m}</span>`;
          if (/null/.test(m)) return `<span style="color:#94a3b8">${m}</span>`;
          return `<span style="color:#fb923c">${m}</span>`;
        },
      );
  }

  private cleanBody(body: Record<string, any>): Record<string, any> {
    return Object.fromEntries(
      Object.entries(body).filter(([, v]) => v !== undefined && v !== null && v !== ''),
    );
  }

  async execute(epId: string, method: string, path: string, body?: Record<string, any>) {
    if (!path || path.includes('undefined') || path.endsWith('/')) {
      this.results[epId] = {
        status: null,
        ok: false,
        ms: 0,
        url: this.baseUrl + path,
        method,
        body: '⚠ Completa los campos requeridos antes de ejecutar.',
        visible: true,
      };
      return;
    }
    this.loading[epId] = true;
    const url = this.baseUrl.replace(/\/$/, '') + path;
    const opts: RequestInit = {
      method,
      headers: { 'Content-Type': 'application/json', Accept: 'application/json' },
    };
    if (body && (method === 'POST' || method === 'PUT'))
      opts.body = JSON.stringify(this.cleanBody(body));
    const start = Date.now();
    try {
      const res = await fetch(url, opts);
      const ms = Date.now() - start;
      const text = await res.text();
      let formatted: string;
      try {
        formatted = this.highlight(JSON.stringify(JSON.parse(text), null, 2));
      } catch {
        formatted = text || '(sin contenido)';
      }
      this.results[epId] = {
        status: res.status,
        ok: res.ok,
        body: formatted,
        ms,
        url,
        method,
        visible: true,
      };
    } catch (err: any) {
      this.results[epId] = {
        status: null,
        ok: false,
        ms: Date.now() - start,
        url,
        method,
        body: `❌ Error de red: ${err.message}\n\nVerifica que el backend esté corriendo en ${this.baseUrl} y que CORS esté habilitado.`,
        visible: true,
      };
    } finally {
      this.loading[epId] = false;
      this.cdr.detectChanges(); // ← agrega esta línea
    }
  }

  execGetProveedores() {
    this.execute('getProveedores', 'GET', '/api/proveedores');
  }
  execGetProveedorById() {
    this.execute('getProveedorById', 'GET', `/api/proveedores/${this.getProveedorById_id}`);
  }
  execPostProveedor() {
    this.execute('postProveedor', 'POST', '/api/proveedores', {
      nombreEmpresa: this.pp_nombreEmpresa,
      ruc: this.pp_ruc,
      nombreContacto: this.pp_nombreContacto,
      telefono: this.pp_telefono,
      email: this.pp_email,
    });
  }
  execPutProveedor() {
    this.execute('putProveedor', 'PUT', `/api/proveedores/${this.putProv_id}`, {
      nombreEmpresa: this.putProv_nombreEmpresa,
      ruc: this.putProv_ruc,
      nombreContacto: this.putProv_nombreContacto,
      telefono: this.putProv_telefono,
      email: this.putProv_email,
    });
  }
  execDeleteProveedor() {
    this.execute('deleteProveedor', 'DELETE', `/api/proveedores/${this.delProv_id}`);
  }

  execGetProductos() {
    this.execute('getProductos', 'GET', '/api/productos');
  }
  execGetProductoById() {
    this.execute('getProductoById', 'GET', `/api/productos/${this.getProdById_id}`);
  }
  execPostProducto() {
    this.execute('postProducto', 'POST', '/api/productos', {
      nombre: this.cp_nombre,
      descripcion: this.cp_descripcion,
      precio: this.cp_precio,
      stock: this.cp_stock,
      categoriaId: this.cp_categoriaId ?? undefined,
      proveedorId: this.cp_proveedorId ?? undefined,
    });
  }
  execPutProducto() {
    this.execute('putProducto', 'PUT', `/api/productos/${this.up_id}`, {
      nombre: this.up_nombre,
      descripcion: this.up_descripcion,
      precio: this.up_precio,
      stock: this.up_stock,
      categoriaId: this.up_categoriaId ?? undefined,
      proveedorId: this.up_proveedorId ?? undefined,
    });
  }
  execDeleteProducto() {
    this.execute('deleteProducto', 'DELETE', `/api/productos/${this.dp_id}`);
  }
  execStockBajo() {
    this.execute('stockBajo', 'GET', `/api/productos/stock-bajo/${this.sb_limite}`);
  }

  execGetCategorias() {
    this.execute('getCategorias', 'GET', '/api/categorias');
  }
  execGetCategoriaById() {
    this.execute('getCategoriaById', 'GET', `/api/categorias/${this.getCatById_id}`);
  }
  execPostCategoria() {
    this.execute('postCategoria', 'POST', '/api/categorias', {
      nombre: this.cc_nombre,
      descripcion: this.cc_descripcion || undefined,
    });
  }
  execPutCategoria() {
    this.execute('putCategoria', 'PUT', `/api/categorias/${this.uc_id}`, {
      nombre: this.uc_nombre,
      descripcion: this.uc_descripcion || undefined,
    });
  }
  execDeleteCategoria() {
    this.execute('deleteCategoria', 'DELETE', `/api/categorias/${this.dc_id}`);
  }

  execGetTransacciones() {
    this.execute('getTransacciones', 'GET', '/api/transacciones');
  }
  execPostTransaccion() {
    this.execute('postTransaccion', 'POST', '/api/transacciones', {
      productoId: this.ct_productoId,
      tipo: this.ct_tipo,
      cantidad: this.ct_cantidad,
      motivo: this.ct_motivo,
    });
  }
  execGetTransaccionesByProducto() {
    this.execute(
      'getTransaccionesByProducto',
      'GET',
      `/api/transacciones/producto/${this.gt_productoId}`,
    );
  }

  protected readonly Number = Number;
}
