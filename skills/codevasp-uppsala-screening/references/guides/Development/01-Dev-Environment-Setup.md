# 01 - Dev Environment Setup

## 1. Join Our Dashboard
* Check that all developers participating in the Travel Rule integration project have created their accounts on the dashboard.
* The dashboard can only be joined by an invitation.
* Request an invitation from the master user.

---

## 2. Environment Setup

### 2-1. Register Your Dev Server

Follow these steps to register your development server in the CodeVASP dashboard:

| Step | Action | Description |
|------|--------|-------------|
| 1 | Navigate to **Development → Environment Info**. | Ensures you can access the environment configuration settings. |
| 2 | Select the **Development** tab. | Confirms you are working in the **sandbox (testing)** environment, not the live production environment where real funds move. |
| 3 | Click the **Register** button. | Opens the form to input your server details. |
| 4 | Select your **Country Code**. | Regulatory compliance is critical in crypto; the system must know which jurisdiction your VASP operates in. |
| 5 | Enter your **URL**. | This is your dev server's address — the endpoint where CodeVASP will send requests and Travel Rule data. |
| 6 | Click **Generate** for the Public/Private Key pair. | This is the most critical step. You are generating a cryptographic key pair to secure all data exchanged with CodeVASP. |
| 7 | Click **Register** (the final confirmation). | Saves your configuration and officially links your server to the CodeVASP platform. |

---

### 2-2. Whitelist Your IP Address

After registering your server, you must configure IP whitelisting for secure communication:

| Step | Action | Description |
|------|--------|-------------|
| 1 | Go to **Development → Environment Info**. | Confirms you can access the environment configuration. |
| 2 | Ensure the **Development** tab is active. | Confirms you are still configuring the test environment, not production. |
| 3 | Click **Request Change**. | Unlocks the **Travel Rule IP WhiteList** section for editing. |
| 4 | Click the **+** button. | Opens the field to add a new trusted IP address. |
| 5 | Enter your IP address followed by the CIDR notation (usually `/32` for a single IP). | This explicitly tells CodeVASP to trust requests originating from this specific IP address. |
| 6 | Click **Confirm**. | Submits your request to the CodeVASP administrator. Note: it can take **up to one business day** to be applied. |
| 7 | ⚠️ **Critical Reminder** | IP whitelisting is a **two-way street**. Not only must you register your IP with CodeVASP, but you must also **whitelist CodeVASP's IPs in your own company firewall** to allow inbound requests. |
