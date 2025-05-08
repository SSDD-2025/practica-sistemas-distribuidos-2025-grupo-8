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

$imageName = "ifernandezru22/gymapp:$version"

docker build -f ./Dockerfile -t $imageName ..

if ($LASTEXITCODE -eq 0) {
    Write-Host "Imagen Docker construida exitosamente: $imageName"
} else {
    Write-Error "Error al construir la imagen Docker."
}
