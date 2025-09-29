(function () {
    'use strict';
    const forms = document.querySelectorAll('.needs-validation');
    Array.prototype.slice.call(forms)
        .forEach(function (form) {
            form.addEventListener('submit', function (event) {
                if (!form.checkValidity()) {
                    event.preventDefault();
                    event.stopPropagation();
                }
                form.classList.add('was-validated');
            }, false);
        });
})();

const messageDiv = document.getElementById("message");
const formInputs = document.querySelectorAll("select, input");

// Hide message when user interacts with any form input
formInputs.forEach(input => {
    input.addEventListener("input", () => {
        if (messageDiv) {
            messageDiv.style.display = "none";
        }
    });
});

// Automatically hide the message after 5 seconds
if (messageDiv) {
    setTimeout(() => {
        messageDiv.style.display = "none";
    }, 3000);
}
