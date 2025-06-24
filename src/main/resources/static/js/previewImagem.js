function previewImagemSelecionada(input) {
    const imagemPreview = document.getElementById('previewImagem');
    const file = input.files[0];

    if (file) {
        const reader = new FileReader();
        reader.onload = function (e) {
            imagemPreview.src = e.target.result;
            imagemPreview.classList.remove('d-none');
        };
        reader.readAsDataURL(file);
    }
}
