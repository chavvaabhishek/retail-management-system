$token = Read-Host "Enter JWT"

Write-Host "Starting 20 concurrent requests..." -ForegroundColor Cyan

# Remove old jobs
Get-Job | Remove-Job -Force -ErrorAction SilentlyContinue

$jobs = @()

# Create 20 background jobs
1..20 | ForEach-Object {

    $requestNumber = $_

    $jobs += Start-Job -ScriptBlock {

        param(
            $requestNumber,
            $token
        )

        $start = Get-Date

        try {

            $headers = @{
                Authorization = "Bearer $token"
            }

            $response = Invoke-RestMethod `
                -Uri "http://localhost:8080/inventory-client/101" `
                -Method Get `
                -Headers $headers

            $end = Get-Date

            [PSCustomObject]@{
                Request  = $requestNumber
                Status   = "SUCCESS"
                Time     = [math]::Round(
                    ($end - $start).TotalSeconds,
                    2
                )
                Response = ($response | ConvertTo-Json -Compress)
            }

        }
        catch {

            $end = Get-Date

            [PSCustomObject]@{
                Request  = $requestNumber
                Status   = "FAILED"
                Time     = [math]::Round(
                    ($end - $start).TotalSeconds,
                    2
                )
                Response = $_.Exception.Message
            }
        }

    } -ArgumentList $requestNumber, $token
}

Write-Host "20 jobs created. Waiting for responses..." -ForegroundColor Yellow

# Wait for all jobs
$jobs | Wait-Job | Out-Null

# Get results
$results = $jobs | Receive-Job

# Display results
$results |
    Sort-Object Request |
    Format-Table -AutoSize

# Clean up jobs
$jobs | Remove-Job -Force

Write-Host ""
Write-Host "Bulkhead test completed." -ForegroundColor Green