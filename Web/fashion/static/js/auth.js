// Auth.js - JavaScript for authentication pages

document.addEventListener('DOMContentLoaded', function() {
    // Initialize all auth functionality
    initializePasswordStrength();
    initializeFormValidation();
    initializePasswordToggle();
    initializeAnimations();
    initializeSocialLogin();
});

// Password strength indicator
function initializePasswordStrength() {
    const passwordInput = document.getElementById('password1');
    if (!passwordInput) return;

    // Create password strength indicator
    const strengthContainer = document.createElement('div');
    strengthContainer.className = 'password-strength';
    strengthContainer.innerHTML = `
        <div class="strength-bar"></div>
        <div class="strength-bar"></div>
        <div class="strength-bar"></div>
        <div class="strength-bar"></div>
    `;
    
    // Create requirements list
    const requirementsContainer = document.createElement('div');
    requirementsContainer.className = 'password-requirements';
    requirementsContainer.innerHTML = `
        <div class="requirement" data-requirement="length">At least 8 characters</div>
        <div class="requirement" data-requirement="uppercase">One uppercase letter</div>
        <div class="requirement" data-requirement="lowercase">One lowercase letter</div>
        <div class="requirement" data-requirement="number">One number</div>
    `;

    // Insert after password input
    passwordInput.parentNode.appendChild(strengthContainer);
    passwordInput.parentNode.appendChild(requirementsContainer);

    // Add event listener
    passwordInput.addEventListener('input', function() {
        updatePasswordStrength(this.value, strengthContainer, requirementsContainer);
    });
}

function updatePasswordStrength(password, strengthContainer, requirementsContainer) {
    const requirements = {
        length: password.length >= 8,
        uppercase: /[A-Z]/.test(password),
        lowercase: /[a-z]/.test(password),
        number: /\d/.test(password)
    };

    // Update requirement indicators
    Object.keys(requirements).forEach(req => {
        const element = requirementsContainer.querySelector(`[data-requirement="${req}"]`);
        if (element) {
            element.className = `requirement ${requirements[req] ? 'valid' : 'invalid'}`;
        }
    });

    // Calculate strength
    const validCount = Object.values(requirements).filter(Boolean).length;
    const strengthBars = strengthContainer.querySelectorAll('.strength-bar');
    
    // Reset all bars
    strengthBars.forEach(bar => {
        bar.className = 'strength-bar';
    });

    // Apply strength styling
    if (validCount >= 1) {
        strengthBars[0].classList.add('weak');
    }
    if (validCount >= 2) {
        strengthBars[1].classList.add('weak');
    }
    if (validCount >= 3) {
        strengthBars[0].classList.remove('weak');
        strengthBars[0].classList.add('medium');
        strengthBars[1].classList.remove('weak');
        strengthBars[1].classList.add('medium');
        strengthBars[2].classList.add('medium');
    }
    if (validCount === 4) {
        strengthBars.forEach(bar => {
            bar.className = 'strength-bar strong';
        });
    }
}

// Form validation
function initializeFormValidation() {
    const forms = document.querySelectorAll('.auth-form');
    
    forms.forEach(form => {
        form.addEventListener('submit', function(e) {
            if (!validateForm(this)) {
                e.preventDefault();
            } else {
                // Add loading state
                const submitBtn = this.querySelector('.btn-auth');
                if (submitBtn) {
                    submitBtn.classList.add('loading');
                    submitBtn.disabled = true;
                    submitBtn.textContent = 'Please wait...';
                }
            }
        });

        // Real-time validation
        const inputs = form.querySelectorAll('input[required]');
        inputs.forEach(input => {
            input.addEventListener('blur', function() {
                validateField(this);
            });

            input.addEventListener('input', function() {
                // Clear previous errors on input
                clearFieldError(this);
            });
        });
    });
}

function validateForm(form) {
    let isValid = true;
    const inputs = form.querySelectorAll('input[required]');
    
    inputs.forEach(input => {
        if (!validateField(input)) {
            isValid = false;
        }
    });

    // Special validation for register form
    if (form.querySelector('#password2')) {
        const password1 = form.querySelector('#password1');
        const password2 = form.querySelector('#password2');
        
        if (password1.value !== password2.value) {
            showFieldError(password2, 'Passwords do not match');
            isValid = false;
        }
    }

    return isValid;
}

function validateField(field) {
    const value = field.value.trim();
    
    // Clear previous errors
    clearFieldError(field);
    
    // Required field validation
    if (field.hasAttribute('required') && !value) {
        showFieldError(field, 'This field is required');
        return false;
    }
    
    // Email validation
    if (field.type === 'email' && value) {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(value)) {
            showFieldError(field, 'Please enter a valid email address');
            return false;
        }
    }
    
    // Password validation
    if (field.type === 'password' && field.id === 'password1' && value) {
        if (value.length < 8) {
            showFieldError(field, 'Password must be at least 8 characters long');
            return false;
        }
    }
    
    return true;
}

function showFieldError(field, message) {
    // Remove existing error
    clearFieldError(field);
    
    // Create error element
    const errorDiv = document.createElement('div');
    errorDiv.className = 'field-error';
    errorDiv.innerHTML = `<small>${message}</small>`;
    
    // Insert after field
    field.parentNode.appendChild(errorDiv);
    
    // Add error styling to field
    field.style.borderColor = '#dc3545';
}

function clearFieldError(field) {
    const existingError = field.parentNode.querySelector('.field-error');
    if (existingError) {
        existingError.remove();
    }
    field.style.borderColor = '';
}

// Password visibility toggle
function initializePasswordToggle() {
    const passwordInputs = document.querySelectorAll('input[type="password"]');
    
    passwordInputs.forEach(input => {
        // Create toggle button
        const toggleBtn = document.createElement('button');
        toggleBtn.type = 'button';
        toggleBtn.className = 'password-toggle';
        toggleBtn.innerHTML = '👁️';
        toggleBtn.style.cssText = `
            position: absolute;
            right: 10px;
            top: 50%;
            transform: translateY(-50%);
            background: none;
            border: none;
            cursor: pointer;
            padding: 5px;
            color: #666;
        `;
        
        // Make parent relative
        input.parentNode.style.position = 'relative';
        input.style.paddingRight = '40px';
        
        // Add toggle button
        input.parentNode.appendChild(toggleBtn);
        
        // Add click event
        toggleBtn.addEventListener('click', function() {
            if (input.type === 'password') {
                input.type = 'text';
                this.innerHTML = '🙈';
            } else {
                input.type = 'password';
                this.innerHTML = '👁️';
            }
        });
    });
}

// Animations
function initializeAnimations() {
    // Fade in animation for auth card
    const authCard = document.querySelector('.auth-card');
    if (authCard) {
        authCard.style.opacity = '0';
        authCard.style.transform = 'translateY(30px)';
        
        setTimeout(() => {
            authCard.style.transition = 'all 0.6s ease';
            authCard.style.opacity = '1';
            authCard.style.transform = 'translateY(0)';
        }, 100);
    }

    // Staggered animation for form groups
    const formGroups = document.querySelectorAll('.form-group');
    formGroups.forEach((group, index) => {
        group.style.opacity = '0';
        group.style.transform = 'translateX(-20px)';
        
        setTimeout(() => {
            group.style.transition = 'all 0.4s ease';
            group.style.opacity = '1';
            group.style.transform = 'translateX(0)';
        }, 200 + (index * 100));
    });
}

// Social login handlers
function initializeSocialLogin() {
    const googleBtn = document.querySelector('.btn-google');
    const facebookBtn = document.querySelector('.btn-facebook');
    
    if (googleBtn) {
        googleBtn.addEventListener('click', function() {
            // Implement Google OAuth
            console.log('Google login clicked');
            // window.location.href = '/auth/google/';
        });
    }
    
    if (facebookBtn) {
        facebookBtn.addEventListener('click', function() {
            // Implement Facebook OAuth
            console.log('Facebook login clicked');
            // window.location.href = '/auth/facebook/';
        });
    }
}

// Utility functions
function showMessage(message, type = 'info') {
    // Create message element
    const messageDiv = document.createElement('div');
    messageDiv.className = `alert alert-${type}`;
    messageDiv.textContent = message;
    
    // Insert at top of form
    const authCard = document.querySelector('.auth-card');
    const authHeader = authCard.querySelector('.auth-header');
    authHeader.insertAdjacentElement('afterend', messageDiv);
    
    // Auto remove after 5 seconds
    setTimeout(() => {
        messageDiv.remove();
    }, 5000);
}

// Auto-dismiss alerts
function initializeAlerts() {
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(alert => {
        setTimeout(() => {
            alert.style.transition = 'opacity 0.3s ease';
            alert.style.opacity = '0';
            setTimeout(() => {
                alert.remove();
            }, 300);
        }, 5000);
    });
}

// Call alert initialization
initializeAlerts();

// Form submission with AJAX (optional)
function submitFormAjax(form, url) {
    const formData = new FormData(form);
    
    fetch(url, {
        method: 'POST',
        body: formData,
        headers: {
            'X-CSRFToken': formData.get('csrfmiddlewaretoken'),
        },
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            showMessage('Registration successful! Redirecting...', 'success');
            setTimeout(() => {
                window.location.href = data.redirect_url || '/';
            }, 2000);
        } else {
            showMessage(data.message || 'An error occurred', 'error');
            // Show field errors
            if (data.errors) {
                Object.keys(data.errors).forEach(field => {
                    const fieldElement = form.querySelector(`[name="${field}"]`);
                    if (fieldElement) {
                        showFieldError(fieldElement, data.errors[field][0]);
                    }
                });
            }
        }
    })
    .catch(error => {
        console.error('Error:', error);
        showMessage('An error occurred. Please try again.', 'error');
    })
    .finally(() => {
        // Remove loading state
        const submitBtn = form.querySelector('.btn-auth');
        if (submitBtn) {
            submitBtn.classList.remove('loading');
            submitBtn.disabled = false;
            submitBtn.textContent = submitBtn.dataset.originalText || 'Submit';
        }
    });
}

// Header scroll effect
window.addEventListener('scroll', function() {
    const header = document.querySelector('.header-main');
    if (header) {
        if (window.scrollY > 100) {
            header.style.background = 'rgba(255, 255, 255, 0.95)';
            header.style.backdropFilter = 'blur(10px)';
        } else {
            header.style.background = 'white';
            header.style.backdropFilter = 'none';
        }
    }
});
