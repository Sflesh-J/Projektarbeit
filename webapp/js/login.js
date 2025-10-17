// Schlichtes Login JavaScript - Minimalistisch und Funktional
$(document).ready(function() {
    const pinInput = $('#pin-input');
    const maxLength = 6;
    
    // Nummer Buttons
    $('.pin-btn[data-num]').on('click', function() {
        const number = $(this).data('num');
        const currentPin = pinInput.val();
        
        if (currentPin.length < maxLength) {
            pinInput.val(currentPin + number);
        }
    });
    
    // Clear Button
    $('.clear-btn').on('click', function() {
        pinInput.val('');
    });
    
    // Error Message schließen
    $('.error-close').on('click', function() {
        $('.error-msg').hide();
    });
    
    // Form Validation
    $('.pin-form').on('submit', function(e) {
        const pin = pinInput.val();
        if (pin.length === 0) {
            e.preventDefault();
            alert('Bitte PIN eingeben');
        }
    });
    
    // Keyboard Support
    $(document).on('keydown', function(e) {
        const key = e.key;
        
        // Zahlen 0-9
        if (/^[0-9]$/.test(key)) {
            const currentPin = pinInput.val();
            if (currentPin.length < maxLength) {
                pinInput.val(currentPin + key);
            }
            e.preventDefault();
        }
        
        // Backspace/Delete
        if (key === 'Backspace' || key === 'Delete') {
            pinInput.val('');
            e.preventDefault();
        }
        
        // Enter
        if (key === 'Enter') {
            $('.pin-form').submit();
            e.preventDefault();
        }
        
        // Escape
        if (key === 'Escape') {
            $('.error-msg').hide();
            e.preventDefault();
        }
    });
});

function renderUsers(usersToRender = filteredUsers) {
    const grid = document.getElementById('anzeige');
    const noResults = document.getElementById('noResults');
    const loading = document.getElementById('loading');

    loading.style.display = 'none';
    
    if (usersToRender.length === 0) {
        grid.style.display = 'none';
        noResults.style.display = 'block';
        return;
    }
    
    grid.style.display = 'grid';
    noResults.style.display = 'none';
    
    grid.innerHTML = usersToRender.map(user => `
        <form class="user-card" action="auth/anmeldung" method="POST">
            <button type="submit" class="user-button" name="uid" value="${user.uid}">
                <div class="user-name">
                    ${user.fname} ${user.name}
                </div>
            </button>
        </form>
    `).join('');
}

document.getElementById('searchInput').addEventListener('input', function(e) {
    const searchTerm = e.target.value.toLowerCase();
    
    filteredUsers = users.filter(user => {
        const fullName = `${user.fname} ${user.name}`.toLowerCase();
        return fullName.includes(searchTerm);
    });
    
    renderUsers();
});

document.addEventListener('DOMContentLoaded', function() {
    setTimeout(() => {
        renderUsers();
    }, 500);
});
