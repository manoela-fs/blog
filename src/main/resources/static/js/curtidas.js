async function toggleCurtida(button) {
    const postId = button.getAttribute('data-post-id');
    try {
        const response = await fetch(`/post/${postId}/curtir`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'X-Requested-With': 'XMLHttpRequest'
            },
            credentials: 'same-origin'
        });

        if (response.redirected) {
            window.location.href = response.url;
            return;
        }

        if (response.status === 401) {
            window.location.href = '/login';
            return;
        }

        if (!response.ok) {
            alert('Erro ao atualizar curtida');
            return;
        }

        const data = await response.json();
        const icon = button.querySelector('i');
        if (data.curtido) {
            icon.classList.remove('bi-heart');
            icon.classList.add('bi-heart-fill');
            button.classList.remove('text-muted');
            button.classList.add('text-danger');
        } else {
            icon.classList.remove('bi-heart-fill');
            icon.classList.add('bi-heart');
            button.classList.remove('text-danger');
            button.classList.add('text-muted');
        }

        button.nextElementSibling.textContent = data.totalCurtidas;
    } catch (error) {
        alert('Erro de comunicação com o servidor');
    }
}

document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('button[data-post-id]').forEach(button => {
        button.addEventListener('click', () => toggleCurtida(button));
    });
});
