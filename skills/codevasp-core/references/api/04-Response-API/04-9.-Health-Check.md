# 04-9. Health Check

The CodeVASP server calls this at a regular interval in order to manage the health status of the VASP list it manages.

## Endpoint

`GET` `/v1/vasp/health`

## Response

| Response | Description |
| :--- | :--- |
| 200 OK | Keep health status up. |
| Other | Change health status to down. |
