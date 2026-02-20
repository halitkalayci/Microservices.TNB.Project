export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  success: boolean;
  username?: string;
  message?: string;
}

export interface SessionInfo {
  authenticated: boolean;
  username?: string;
}
