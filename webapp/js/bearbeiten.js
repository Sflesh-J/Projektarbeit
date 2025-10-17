$(function() {
    // Dropdown-Funktionalität
    $('.dropdown-toggle').on('click', function(e) {
        e.stopPropagation();
        $('.dropdown').not($(this).parent()).removeClass('open');
        $(this).parent('.dropdown').toggleClass('open');
    });

    $('.dropdown-menu').on('click', function(e) {
        e.stopPropagation();
    });

    $(document).on('click', function() {
        $('.dropdown').removeClass('open');
    });

    let warenkorb = {};

    $('.drink-card.add-item').on('click', addItem);
    
    items.forEach(function (item) {
        const uid = item.uid;
        const itempreisID = item.itempreisID;
        const name = item.name;
        const preisInCent = item.preis;
        
        if (name && preisInCent !== null) {
            console.log('Adding existing item to cart:', { name, preisInCent, uid, itempreisID });
            addToWarenkorb(name, preisInCent, uid, itempreisID);
        }
    });

    function addItem() {
        const $this = $(this);
        const uid = $this.data('uid');
        const itempreisID = $this.data('itempreis-id');
        const name = $this.data('name') || $this.find('.drink-name').text().trim();
        
        let preisInCent = $this.data('price');
        if (!preisInCent) {
            preisInCent = parsePreisFromText($this.find('.drink-price').text().trim());
        }
        
        console.log('Adding new item:', { name, preisInCent, uid, itempreisID });
        
        if (name && preisInCent !== null) {
            addToWarenkorb(name, preisInCent, uid, itempreisID);
            
            $this.addClass('added');
            setTimeout(() => $this.removeClass('added'), 200);
        } else {
            console.error('Invalid item data:', { name, preisInCent, uid, itempreisID });
        }
    }

    function parsePreisFromText(preisText) {
        if (!preisText) return null;
        
        if (preisText.includes('€')) {
            const euroValue = parseFloat(preisText.replace('€', '').replace(',', '.').trim());
            return Math.round(euroValue * 100); 
        } else if (preisText.includes('ct')) {
            return parseInt(preisText.replace('ct', '').trim());
        }
        return null;
    }

    function addToWarenkorb(name, preisInCent, uid, itempreisID) {
        console.log('Adding to warenkorb:', { name, preisInCent, uid, itempreisID });
        
        if (warenkorb[name]) {
            warenkorb[name].anzahl += 1;
        } else {
            warenkorb[name] = {
                preisInCent: preisInCent,
                anzahl: 1,
                uid: uid,
                itempreisID: itempreisID
            };
        }
        updateWarenkorb();
    }

    function updateWarenkorb() {
        const $warenkorbListe = $('#warenkorb-liste');
        const $form = $('#warenkorb-form');
		const $warenkorbHidden = $('#hidden-warenkorb');

        // Vorherige Hidden-Inputs entfernen
        $warenkorbListe.empty();
		$warenkorbHidden.empty();

        let gesamtSummeInCent = 0;
        let hatItems = false;

        if (Object.keys(warenkorb).length === 0) {
            $warenkorbListe.append('<li class="empty-cart">Warenkorb ist leer</li>');
        } else {
            for (let name in warenkorb) {
                const item = warenkorb[name];
                const itemSummeInCent = item.preisInCent * item.anzahl;
                gesamtSummeInCent += itemSummeInCent;
                hatItems = true;

                const $listItem = $(`
                    <li class="warenkorb-item">
                        <div class="item-info">
                            <div class="item-name">${name}</div>
                            <div class="item-details">${formatPreis(item.preisInCent)} × ${item.anzahl} = ${formatPreis(itemSummeInCent)}</div>
                        </div>
                        <div class="item-controls">
                            <button type="button" class="quantity-btn remove-btn" data-name="${name}">−</button>
                            <span class="quantity">${item.anzahl}</span>
                            <button type="button" class="quantity-btn add-btn" data-name="${name}">+</button>
                        </div>
                    </li>
                `);

                $warenkorbListe.append($listItem);

                // ✅ Hidden-Inputs für Form-Submit erstellen
                $warenkorbHidden.append(`<input type="hidden" name="itemW" value="${item.uid}:${item.anzahl}">`);
                $warenkorbHidden.append(`<input type="hidden" name="itempreisID" value="${item.itempreisID}">`);
            }
        }

        $('.sum-value').html(formatPreis(gesamtSummeInCent));
        $('.bestaetigen').prop('disabled', !hatItems);
        
        console.log('Warenkorb updated:', warenkorb);
    }

    function formatPreis(preisInCent) {
        if (preisInCent < 100) {
            return `${preisInCent} ct`;
        } else {
            const euroValue = (preisInCent / 100).toFixed(2).replace('.', ',');
            return `${euroValue}<span class="euro">€</span>`;
        }
    }

	
    $(document).on('click', '.remove-btn', function() {
        const name = $(this).data('name');
        
        if (warenkorb[name]) {
            warenkorb[name].anzahl -= 1;
            
            if (warenkorb[name].anzahl <= 0) {
                delete warenkorb[name];
            }
            updateWarenkorb();
        }
    });

    $(document).on('click', '.add-btn', function() {
        const name = $(this).data('name');
        
        if (warenkorb[name]) {
            warenkorb[name].anzahl += 1;
            updateWarenkorb();
        }
    });

    $(document).on('keydown', function(e) {
        if (e.key === 'Escape') {
            $('.dropdown').removeClass('open');
        }
    });

    // CSS-Styles
    $('<style>')
        .prop('type', 'text/css')
        .html(`
            .drink-card.added {
                transform: scale(1.05) !important;
                background: lighten(#ede5d2, 5%) !important;
                border-color: #d1bc96 !important;
            }
            
            .quantity-btn {
                font-size: 1.2rem;
                line-height: 1;
                user-select: none;
            }
            
            .remove-btn:hover {
                background: #e74c3c !important;
                color: white !important;
            }
            
            .add-btn:hover {
                background: #27ae60 !important;
                color: white !important;
            }
            
        `)
        .appendTo('head');

    // ✅ Initial den Warenkorb aktualisieren (nach dem Laden der vorhandenen Items)
    updateWarenkorb();
});