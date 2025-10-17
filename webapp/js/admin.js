$(document).ready(function() {
    initializeAdminInterface();
    bindEventHandlers();
    
    setTimeout(function() {
        $('.admin-form input:visible:first').focus();
    }, 100);
});

function initializeAdminInterface() {
    const today = new Date().toISOString().split('T')[0];
    $('#new-item-datum').val(today);
    initializeInputFormatters();
}

function bindEventHandlers() {
    $('#neuer-Benutzer').on('click', handleNewUserClick);
    $('#neues-Item').on('click', handleNewItemClick);
    $('#Gruppenbuchung').on('click', handleGroupBookingClick);
    $('.btn-close').on('click', handleCloseClick);
    
    $('#new-user-form').on('submit', handleNewUserSubmit);
    $('#new-item-form').on('submit', handleNewItemSubmit);
    
    $('#close-new-item-form, #cancel-new-item').on('click', function() {
        location.reload();
    });
    
    $('#new-item-overlay').on('click', function(e) {
        if (e.target === this) {
            $(this).remove();
        }
    });
    
    $(document).on('click', '#user-grid .select', handleUserSelectClick);
    $('button[name="method"][value="deaktivieren"]').on('click', handleDeactivationClick);
    $('.admin-form').on('submit', handleFormSubmitLoading);
    $(document).on('keydown', handleKeyboardShortcuts);
    
    // Group booking action buttons
    $(document).on('click', '#gruppenbuchung-section button[name="method"]', handleGroupBookingAction);
}

function handleNewUserClick() {
    resetCardSelections();
    hideAllSections();
    resetNavigationStyles();
    
    $('#neuer-Benutzer').css({"background": "#6b4e28", "color": "#fff"});
    $('#new-user-section').show();
    $('.select').hide();
    $('.edit').show();
    
    disableCardClickFunctionality();
    
    setTimeout(() => $('#new-vorname').focus(), 100);
}

function handleNewItemClick() {
    resetCardSelections();
    hideAllSections();
    resetNavigationStyles();
    
    $('#neues-Item').css({"background": "#6b4e28", "color": "#fff"});
    $('#new-item-section').show();
    
    setTimeout(() => $('#new-item-name').focus(), 100);
}

function handleGroupBookingClick() {
    hideAllSections();
    resetNavigationStyles();
    
    $('#Gruppenbuchung').css({"background": "#6b4e28", "color": "#fff"});
    $('#gruppenbuchung-section').show();
    $('.select').show();
    $('.edit').hide();
    
    enableCardClickFunctionality();
    clearGroupBookingInputs();
}

function handleGroupBookingAction(e) {
    // ensure fresh inputs each click
    clearGroupBookingInputs();
    
    const selectedCards = $('#user-grid .card.selected');
    
    if (selectedCards.length === 0) {
        e.preventDefault();
        alert('Bitte wähle mindestens einen Benutzer aus.');
        return false;
    }
    
    selectedCards.each(function() {
        const userId = $(this).data('uid');
        const hiddenInput = $('<input type="hidden" name="userIds" value="' + userId + '">');
        $('#inputs').append(hiddenInput);
    });
    // Let native form submission proceed so the clicked button's name/value (method) is included
}

function clearGroupBookingInputs() {
    $('#inputs').empty();
}

function handleCloseClick() {
    location.reload();
    disableCardClickFunctionality();
}

function handleNewUserSubmit(e) {
    if (!validateNewUserForm()) {
        e.preventDefault();
        return false;
    }
}

function handleNewItemSubmit(e) {
    if (!validateNewItemForm()) {
        e.preventDefault();
        return false;
    }
}

function handleUserSelectClick(e) {
    e.preventDefault();
    
    const $btn = $(this);
    const $card = $btn.closest('.card');
    
    if ($card.hasClass('selected')) {
        $card.removeClass('selected');
        $btn.text('Auswählen');
    } else {
        $card.addClass('selected');
        $btn.text('Abwählen');
    }
}

function handleUserCardClick(e) {
    if (!$('#gruppenbuchung-section').is(':visible')) {
        return;
    }
    
    if (!$(e.target).is('a')) {
        const $btn = $(this).find('.select');
        $btn.click();
    }
}

function enableCardClickFunctionality() {
    $(document).off('click', '#user-grid .card', handleUserCardClick);
    $(document).on('click', '#user-grid .card', handleUserCardClick);
}

function disableCardClickFunctionality() {
    $(document).off('click', '#user-grid .card', handleUserCardClick);
}

function handleDeactivationClick(e) {
    const form = $(this).closest('form');
    const isUser = form.find('input[name="uid"]').length > 0;
    const isItem = form.find('input[name="Item_uid"]').length > 0;
    
    let confirmText = '';
    if (isUser) {
        const userName = $('.user-header h3').text() || 'diesen Benutzer';
        confirmText = `Möchten Sie wirklich ${userName} deaktivieren?`;
    } else if (isItem) {
        const itemName = $('.item-header h3').text() || 'dieses Item';
        confirmText = `Möchten Sie wirklich ${itemName} deaktivieren?`;
    } else {
        confirmText = 'Möchten Sie wirklich deaktivieren?';
    }
    
    if (!confirm(confirmText)) {
        e.preventDefault();
        return false;
    }
}

function handleFormSubmitLoading() {
    const $submitBtn = $(this).find('button[type="submit"]');
    const originalText = $submitBtn.text();
    
    setTimeout(function() {
        $submitBtn.prop('disabled', false).text(originalText);
    }, 5000);
}

function handleKeyboardShortcuts(e) {
    if (e.key === 'Escape') {
        $('.user-list-overlay, .form-overlay').remove();
    }
}

function resetCardSelections() {
    $('.card').each(function() {
        const $card = $(this);
        $card.removeClass('selected');
        $('.name, .role, .id', $card).css({"color": ""});
        
        const $selectBtn = $('.select', $card);
        $selectBtn.css({"color": ""});
        $selectBtn.text('Auswählen');
    });
}

function hideAllSections() {
    $('#gruppenbuchung-section, #new-user-section, #new-item-section').hide();
}

function resetNavigationStyles() {
    $('#neuer-Benutzer, #Gruppenbuchung, #neues-Item').css({
        "color": "",
        "background": ""
    });
}

function initializeInputFormatters() {
    $('input[name="betrag"]').on('input', function() {
        let value = $(this).val().replace(/[^\d.,]/g, '');
        value = value.replace(',', '.');
        $(this).val(value);
    });
    
    $('input[name="pin"], #new-pin').on('input', function() {
        let value = $(this).val().replace(/[^\d]/g, '');
        if (value.length > 10) {
            value = value.substring(0, 10);
        }
        $(this).val(value);
    });
}

function validateNewUserForm() {
    const vorname = $('#new-vorname').val().trim();
    const nachname = $('#new-nachname').val().trim();
    const pin = $('#new-pin').val().trim();
    
    if (!vorname || !nachname || !pin) {
        alert('Bitte füllen Sie alle Pflichtfelder aus.');
        return false;
    }
    
    if (pin.length < 4) {
        alert('PIN muss mindestens 4 Zeichen lang sein.');
        return false;
    }
    
    return true;
}

function validateNewItemForm() {
    const name = $('#new-item-name').val().trim();
    const preis = $('#new-item-preis').val().trim();
    const datum = $('#new-item-datum').val();
    
    if (!name || !preis || !datum) {
        alert('Bitte füllen Sie alle Pflichtfelder aus.');
        return false;
    }
    
    if (parseFloat(preis) <= 0) {
        alert('Preis muss größer als 0 sein.');
        return false;
    }
    
    return true;
}