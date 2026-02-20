import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { LoginResponse } from '../../core/models/login.model';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent {
  loginForm: FormGroup;
  errorMessage: string | null = null;
  loading = false;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      username: ['', [Validators.required]],
      password: ['', [Validators.required]],
    });
  }

  onSubmit(): void {
    if (this.loginForm.invalid) {
      return;
    }

    this.loading = true;
    this.errorMessage = null;

    this.authService.login(this.loginForm.value).subscribe({
      next: (response: LoginResponse) => {
        this.loading = false;
        if (response.success) {
          this.router.navigate(['/products']);
        } else {
          this.errorMessage = response.message ?? 'Giriş başarısız.';
        }
      },
      error: (err: any) => {
        this.loading = false;
        this.errorMessage =
          err.error?.message ?? 'Giriş başarısız. Bilgilerinizi kontrol edin.';
      },
    });
  }
}
