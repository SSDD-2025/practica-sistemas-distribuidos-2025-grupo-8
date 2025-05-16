if ($args.Count -ne 1) {
    Write-Error "Uso: .\publish_compose.ps1 <usuario>"
    exit 1
}
$usuario = $args[0]
$env:DOCKER_USER = $usuario
docker compose -f ./docker-compose.prod.yml publish $usuario/gymapp-compose:latest --with-env