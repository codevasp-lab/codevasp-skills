# Dev 12 - Go-Live Preparation

## 1. Checklist
### 1-1. Have you implemented Travel Rule for API-based withdrawals?

If you support withdrawals through APIs, Travel Rule compliance must be enforced
as well. This is usually managed by whitelisted(pre-registering) addresses. For more details, please refer to the guide linked below.

### 1-2. Have you implemented VASP list management?
VASPs are advised to maintain a separate list of VASPs allowed for deposits and
withdrawals. The list retrieved from the CodeVASP API may differ for each VASP
depending on internal policies, especially VASPs in countries with stricter
regulations. Displaying VASPs that cannot actually be used may harm the user
experience, so it is recommended to manage an internally approved list separately.

### 1-3. Have you implemented birthdate inclusion in the originator information?
While Travel Rule requirements vary by jurisdiction, verifying a user’s date of birth is emerging as a key global regulatory trend. In regions such as Europe and Hong Kong, including the birthdate is already mandatory. To ensure interoperability between VASPs, please collect and transmit the originator’s date of birth.

### 1-4. Have you implemented the 'Report Transfer Result' API to function as part of your actual transfer process?
Some VASPs, depending on their internal policies, do not proceed with asset deposits unless the txid has been properly submitted. Many VASPs consider correct implementation of this process—aligned with the standard—a key requirement during integration reviews.
Please ensure your production environment reflects all scenarios tested in the development environment, in accordance with CodeVASP’s guidelines.

## 2. Go-Live Preparation
### 2-1. Request/Response test complete
### 2-2. Dev environment test
- To test with another VASP, please contact the CodeVASP team.
### 2-3. Live environment test

### 2-4. Finalizing go-live schedule and announcement
- Please finalize it through internal discussions and share it with the CodeVASP team.
- CodeVASP team will make an announcement visible to all customers.🎉
### 2-5. Development Completed!
- DD process for each VASP will follow afterward.
 