// index.js - Main JavaScript for e-commerce

document.addEventListener('DOMContentLoaded', function() {
    // Initialize cart count
    updateCartCount();
    
    // Smooth scroll for anchor links
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function (e) {
            e.preventDefault();
            const target = document.querySelector(this.getAttribute('href'));
            if (target) {
                target.scrollIntoView({
                    behavior: 'smooth'
                });
            }
        });
    });

    // Animate elements on scroll
    const observerOptions = {
        threshold: 0.1,
        rootMargin: '0px 0px -50px 0px'
    };

    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.style.opacity = '1';
                entry.target.style.transform = 'translateY(0)';
            }
        });
    }, observerOptions);

    document.querySelectorAll('.product-card, .category-card').forEach(card => {
        card.style.opacity = '0';
        card.style.transform = 'translateY(20px)';
        card.style.transition = 'all 0.6s ease';
        observer.observe(card);
    });

    // Add to cart functionality
    document.querySelectorAll('.add-to-cart-btn').forEach(btn => {
        btn.addEventListener('click', function(e) {
            e.preventDefault();
            const productoId = this.getAttribute('data-producto-id');
            
            if (!productoId) {
                // Redirect to product detail page if no ID
                const productCard = this.closest('.product-card');
                const productLink = productCard.querySelector('a[href*="producto"]');
                if (productLink) {
                    window.location.href = productLink.href;
                }
                return;
            }
            
            // Get available sizes
            fetch(`/ajax/tallas/${productoId}/`)
                .then(response => response.json())
                .then(data => {
                    if (data.success && data.tallas.length > 0) {
                        // Show size selection modal or redirect to product page
                        window.location.href = `/producto/${productoId}/`;
                    } else {
                        alert('Este producto no tiene tallas disponibles');
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    window.location.href = `/producto/${productoId}/`;
                });
        });
    });

    // Newsletter form
    const newsletterForm = document.querySelector('.newsletter-form');
    if (newsletterForm) {
        newsletterForm.addEventListener('submit', function(e) {
            e.preventDefault();
            const email = this.querySelector('input').value;
            if (email) {
                alert(`¡Gracias por suscribirte con el correo: ${email}!`);
                this.querySelector('input').value = '';
            }
        });
    }

    // Header scroll effect
    let lastScrollTop = 0;
    window.addEventListener('scroll', function() {
        const header = document.querySelector('.header-main');
        if (header) {
            const scrollTop = window.pageYOffset || document.documentElement.scrollTop;
            
            if (scrollTop > lastScrollTop && scrollTop > 100) {
                header.style.transform = 'translateY(-100%)';
            } else {
                header.style.transform = 'translateY(0)';
            }
            
            lastScrollTop = scrollTop;
        }
    });
});

// Update cart count
function updateCartCount() {
    fetch('/ajax/carrito/count/')
        .then(response => response.json())
        .then(data => {
            const cartCountElements = document.querySelectorAll('#cart-count, .cart-badge');
            cartCountElements.forEach(el => {
                if (el) {
                    el.textContent = data.count || 0;
                    if (data.count > 0) {
                        el.style.display = 'inline-block';
                    } else {
                        el.style.display = 'none';
                    }
                }
            });
        })
        .catch(error => {
            console.error('Error updating cart count:', error);
        });
}

// Add to cart function
function addToCart(productoId, tallaId, cantidad) {
    const formData = new FormData();
    formData.append('producto_id', productoId);
    formData.append('talla_id', tallaId);
    formData.append('cantidad', cantidad);
    formData.append('csrfmiddlewaretoken', getCookie('csrftoken'));
    
    return fetch('/carrito/agregar/', {
        method: 'POST',
        body: formData
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            updateCartCount();
            return data;
        } else {
            throw new Error(data.message || 'Error al agregar al carrito');
        }
    });
}

// Get CSRF token from cookies
function getCookie(name) {
    let cookieValue = null;
    if (document.cookie && document.cookie !== '') {
        const cookies = document.cookie.split(';');
        for (let i = 0; i < cookies.length; i++) {
            const cookie = cookies[i].trim();
            if (cookie.substring(0, name.length + 1) === (name + '=')) {
                cookieValue = decodeURIComponent(cookie.substring(name.length + 1));
                break;
            }
        }
    }
    return cookieValue;
}

// Show notification
function showNotification(message, type = 'success') {
    const notification = document.createElement('div');
    notification.className = `notification notification-${type}`;
    notification.textContent = message;
    notification.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        padding: 15px 20px;
        background: ${type === 'success' ? '#4caf50' : '#f44336'};
        color: white;
        border-radius: 5px;
        box-shadow: 0 4px 10px rgba(0,0,0,0.2);
        z-index: 10000;
        animation: slideIn 0.3s ease;
    `;
    
    document.body.appendChild(notification);
    
    setTimeout(() => {
        notification.style.animation = 'slideOut 0.3s ease';
        setTimeout(() => {
            notification.remove();
        }, 300);
    }, 3000);
}

// Add CSS animations
const style = document.createElement('style');
style.textContent = `
    @keyframes slideIn {
        from {
            transform: translateX(100%);
            opacity: 0;
        }
        to {
            transform: translateX(0);
            opacity: 1;
        }
    }
    
    @keyframes slideOut {
        from {
            transform: translateX(0);
            opacity: 1;
        }
        to {
            transform: translateX(100%);
            opacity: 0;
        }
    }
    
    .cart-badge {
        background: #f44336;
        color: white;
        border-radius: 50%;
        padding: 2px 6px;
        font-size: 0.7rem;
        margin-left: 5px;
        display: inline-block;
        min-width: 18px;
        text-align: center;
    }
`;
document.head.appendChild(style);
