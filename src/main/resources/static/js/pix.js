function copiarPix() {
    // Remove pontos, barra e hífen para copiar apenas os números limpos do CNPJ, facilitando a colagem nos apps de bancos
    const cnpjLimpo = "14173181000162";
    
    // Executa a escrita automática na área de transferência do computador/celular
    navigator.clipboard.writeText(cnpjLimpo).then(() => {
        const alerta = document.getElementById('alertaCopia');
        alerta.classList.remove('d-none'); // Exibe a caixa verde tirando a classe de ocultação do Bootstrap
        
        // Oculta após 4 segundos automaticamente
        setTimeout(() => {
            alerta.classList.add('d-none');
        }, 4000);
    });
}