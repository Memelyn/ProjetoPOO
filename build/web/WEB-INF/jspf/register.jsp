<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css">
    <script src="https://cdn.jsdelivr.net/npm/vue@3.2.45/dist/vue.global.min.js"></script>
</head>
<body>
    <div id="app" class="container mt-5">
        <h1 class="text-center">Register</h1>
        <div class="card p-4 shadow">
            <form @submit.prevent="register">
                <div class="mb-3">
                    <label for="login" class="form-label">Login</label>
                    <input type="text" class="form-control" id="login" v-model="registerData.login" required>
                </div>
                <div class="mb-3">
                    <label for="name" class="form-label">Name</label>
                    <input type="text" class="form-control" id="name" v-model="registerData.name" required>
                </div>
                <div class="mb-3">
                    <label for="password" class="form-label">Password</label>
                    <input type="password" class="form-control" id="password" v-model="registerData.password" required>
                </div>
                <button type="submit" class="btn btn-primary w-100">Register</button>
            </form>
            <div class="mt-3 text-center">
                <a href="index.html">Already have an account? Login here.</a>
            </div>
        </div>
        <div v-if="message" class="alert mt-3" :class="{'alert-danger': error, 'alert-success': !error}">
            {{ message }}
        </div>
    </div>

    <script>
        const { createApp } = Vue;
        createApp({
            data() {
                return {
                    registerData: {
                        login: '',
                        name: '',
                        password: ''
                    },
                    message: '',
                    error: false
                };
            },
            methods: {
                async register() {
                    try {
                        const response = await fetch('/api/users', {
                            method: 'POST',
                            headers: { 'Content-Type': 'application/json' },
                            body: JSON.stringify(this.registerData)
                        });

                        if (!response.ok) {
                            const errorData = await response.json();
                            this.message = errorData.error || 'Registration failed';
                            this.error = true;
                        } else {
                            this.message = 'Registration successful!';
                            this.error = false;
                        }
                    } catch (error) {
                        this.message = 'An error occurred: ' + error.message;
                        this.error = true;
                    }
                }
            }
        }).mount('#app');
    </script>
</body>
</html>
