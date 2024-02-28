function addItem() {
    // Get the values entered by the user for the new item
    var newItemName = document.getElementById('newItemName').value;
    var newItemPrice = document.getElementById('newItemPrice').value;

    // Prepare the request options for the fetch API
    var requestOptions = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            name: newItemName,
            price: newItemPrice
        })
    };

    // Make a POST request to the server's /grocery/items endpoint
    fetch('http://localhost:8080/grocery/items', requestOptions)
        .then(handleResponse)
        .then(data => {
            console.log('Response data:', data);
            showSuccessMessage('Item added successfully!', 'messageItem');
        })
        .catch(error => {
            handleError(error);
        });
}

function deleteItem() {
    // Get the item name from the input field
    var itemName = document.getElementById('itemName').value;

    var requestOptions = {
        method: 'DELETE',
    };

    // Make a DELETE request to the server's /grocery/items endpoint
    fetch('http://localhost:8080/grocery/items/' + itemName, requestOptions)
        .then(handleResponse)
        .then(data => {
            console.log('Response data:', data);
            showSuccessMessage('Item deleted successfully!', 'messageItemDelete');
        })
        .catch(error => {
            showErrorMessage('Error in deleting the item!', 'messageItemDelete');
        });
}

function getItems() {
    // Make a GET request to the server's /grocery/items endpoint
    fetch('http://localhost:8080/grocery/items')
        .then(handleResponse)
        .then(data => {
            // Display the results using the displayResults function
            displayResults('Items:', data, 'messageGetItems');
        })
        .catch(error => {
           handleError(error);
        });
}


function addDeal() {
    // Get the values entered by the user for the new deal
    var dealType = document.getElementById('newDealType').value;
    var dealItems = document.getElementById('newDealItems').value.split(',');

    // Prepare the request options for the fetch API
    var requestOptions = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            type: dealType,
            items: dealItems
        })
    };

    // Make a POST request to the server's /grocery/deals endpoint
    fetch('http://localhost:8080/grocery/deals', requestOptions)
        .then(handleResponse)
        .then(data => {
            console.log('Response data:', data);
            showSuccessMessage('Deal added successfully!', 'messageDeal');
        })
        .catch(error => {
            showErrorMessage('Error in adding deal!', 'messageDeal');
        });
}

function deleteDeal() {
    var dealType = document.getElementById('dealType').value;

    var requestOptions = {
        method: 'DELETE',
    };

    // Make a DELETE request to the server's /grocery/deals endpoint
    fetch('http://localhost:8080/grocery/deals/' + dealType, requestOptions)
        .then(handleResponse)
        .then(data => {
            console.log('Response data:', data);
            showSuccessMessage('Deal deleted successfully!', 'messageDealDelete');
        })
        .catch(error => {
            showErrorMessage('Error in deleting the deal!', 'messageDealDelete');
        });
}

function getDeals() {
    // Make a GET request to the server's /grocery/deals endpoint
    fetch('http://localhost:8080/grocery/deals')
        .then(handleResponse)
        .then(data => {
            displayResults('Deals:', data, 'messageGetDeals');
        })
        .catch(error => {
         handleError(error);
        });
}

function scanItems() {
    // Get the scanned items from the input field and split them into an array
    var scannedItems = document.getElementById('scannedItems').value.split(',');

    // Prepare the request options
    var requestOptions = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        // Convert the array of scanned items to JSON and set it as the request body
        body: JSON.stringify(scannedItems)
    };

    // Make a POST request to the server's /grocery/scan endpoint
    fetch('http://localhost:8080/grocery/scan', requestOptions)
        .then(handleResponse)
        .then(data => {
            console.log('Response data:', data);
            document.getElementById('resultMessage').innerHTML = 'Scan successful! Total cost: ' + data + ' aws';

        })
        .catch(error => {
            showErrorMessage('Error in scanning the items!', 'resultMessage');
        });
}

function showSuccessMessage(message, elementId) {
    var targetContainer = document.getElementById(elementId);

    // Check if the target container exists
    if (!targetContainer) {
        console.error('Target container not found:', elementId);
        return;
    }

    // Create a new div for the success message
    var successMessage = document.createElement('div');
    successMessage.className = 'success-message';
    successMessage.textContent = message;

    // Append the message to the target container
    targetContainer.appendChild(successMessage);

    // Clear the message after a certain time (3 seconds)
    setTimeout(function () {
        targetContainer.removeChild(successMessage);
    }, 3000);
}

function showErrorMessage(message, elementId) {
    var messagesContainer = document.getElementById(elementId);

    var errorMessage = document.createElement('div');
    errorMessage.className = 'error-message';
    errorMessage.textContent = message;

    messagesContainer.appendChild(errorMessage);

    setTimeout(function () {
        messagesContainer.removeChild(errorMessage);
    }, 5000); // Clear the message after 5 seconds
}

function displayResults(title, data, elementId) {
    // Clear previous results
    document.getElementById(elementId).innerHTML = '';

    // Create a new div for the results
    var resultsDiv = document.createElement('div');
    resultsDiv.className = 'results';
    resultsDiv.innerHTML = `<h3>${title}</h3><pre>${JSON.stringify(data, null, 2)}</pre>`;

    // Append the results to the result container
    document.getElementById(elementId).appendChild(resultsDiv);
}

function handleResponse(response) {
    if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
    }

    const contentType = response.headers.get('content-type');

    if (contentType && contentType.includes('application/json')) {
        return response.json();
    } else {
        return response.text();
    }
}

function handleError(error) {
    console.error('There was a problem with the fetch operation:', error);
    alert('Error processing the request. Please check the console for details.');
}




