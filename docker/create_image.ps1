if ($args.Count -ne 1) {
    Write-Error "Uso: .\create_image.ps1 <usuario>"
    exit 1
}
$usuario = $args[0]

$pomPath = "../gymapp/pom.xml"

if (-Not (Test-Path $pomPath)) {
    Write-Error "No se encontró el archivo pom.xml en la carpeta superior."
    exit 1
}

[xml]$pom = Get-Content $pomPath
$version = $pom.project.version

if (-Not $version) {
    Write-Error "No se pudo extraer la versión del pom.xml."
    exit 1
}

$imageName = "$usuario/gymapp:$version"

docker build -f ./Dockerfile -t $imageName ..

if ($LASTEXITCODE -eq 0) {
    Write-Host "Imagen Docker construida exitosamente: $imageName"
} else {
    Write-Error "Error al construir la imagen Docker."
}
