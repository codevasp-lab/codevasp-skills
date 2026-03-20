# 10 - Returning Errors

This chapter describes the error response which is used in common by all APIs of the CodeVASP solution.
All APIs in the code are designed to return an error response which combines Status Code, Error Type, and ErrorMsg described below when an error occurs while processing the request.

Please make an implementation so that even the VASP API of the CodeVASP implemented in the VASP server returns an error according to the classification as below.

Please note that the error returned by the CodeVASP API server and the error returned by the VASP API server may differ depending on the status code. Even for the same ErrorType, the subject that returns an error is different depending on the status code.

>📌'invalid' and 'denied'
> An `invalid` result for the 'Search VASP by Wallet' request or a `denied` result for the 'Asset Transfer Authorization' request is not considered an error, as both indicate that the request was processed successfully.

## 422 Validation Error

This error is returned by the CodeVASP server.

This is an error returned by the CodeVASP API server when failing to check the parameter of a message received by the CodeVASP API server. This applies to both a message processed directly by the CodeVASP API server and a message delivered to the VASP API.

* errorType: Error type
  * INVALID\_TARGET\_VASP\_ID: The Entity ID of the VASP entered as a parameter is not valid.
  * MISSING\_REQUIRED\_MSG\_FIELD: A required message field is missing when processing a request.
  * MISSING\_HEADER\_FIELD: A required header field is missing.
  * INVALID\_MSG\_FIELD\_VALUE: The value of the message field is not valid.
  * DUPLICATED\_HEADER\_NONCE: This is a case where the Nonce value of the message header is retransmitted within 1 minute. Please set this to a value that changes each time a request is sent.
  * INVALID\_HEADER\_SIGNATURE: This is a case where the signature verification of the message header has failed.
  * INVALID\_RECEIVER\_PUBLIC\_KEY: This is the case where the other public key of the request header is not valid. The public key of the other VASP may have been changed.
  * ILLEGAL\_FORMAT\_OF\_ORIGIN: The character string format of the header origin is abnormal. Please send in the \{AllianceName}:\{VaspEntityId} format.
  * INVALID\_ORIGIN: The value of header origin is not correct. VaspEntityId cannot be found or AllianceName is not valid.
  * INVALID_BENEFICIARY: The beneficiary VASP is not valid. This occurs when the API requires a beneficiary VASP but the provided VASP is invalid or cannot be found.
* errorMsg: This is used to convey detailed information on the ErrorType. This is optional. 
```
{
  "errorType": "INVALID_TARGET_VASP_ID",
  "errorMsg": "VaspEntityId received is."
}
```

## 408 Request Timeout

This error is returned by the CodeVASP API server.

This is an error returned by the CodeVASP API server when the CodeVASP API server passes a request to the VASP API server, which processes the request actually, and when the response time is exceeded.

* errorType: Error type
  * BENEFICIARY\_VASP\_REQUEST\_TIMEOUT
* errorMsg: This is used to convey detailed information on the ErrorType. This is optional. 

```
{
  "errorType": "BENEFICIARY_VASP_REQUEST_TIMEOUT",
  "errorMsg": "Beneficiary VASP does not respond."
}
```

## 429 Too Many Requests

This error is returned by the CodeVASP API server.
Specific APIs that can affect the performance of the entire system limit the number of requests that can be sent per second by source IP. This returns this error if the number of requests exceeds the limit value.

* errorType: Error type
  * TOO\_MANY\_REQUESTS
* errorMsg: This is used to convey detailed information on the ErrorType. This is optional. 
```
{
  "errorType": "TOO_MANY_REQUESTS",
  "errorMsg": "The request has exceeded the limit (40) per minute, source IP."
}
```

## 500 Internal Server Error

This error is returned by the CodeVASP API server.

This returns this kind of error when an error occurs while the CodeVASP API processes a request directly.

* errorType: Error type
  * RECEIVED\_WRONG\_JSON\_MESSAGE: The JSON message responded by the recipient is not readable.
  * RECEIVED\_MSG\_MISSING\_FIELD: There are missing fields in the message responded by the recipient.
  * CODE\_SERVER\_INTERNAL\_ERROR: An error occurred while the CodeVASP server processes a request.
* errorMsg: This is used to convey detailed information on the ErrorType. This is optional. 

```
{
  "errorType": "CODE_SERVER_INTERNAL_ERROR",
  "errorMsg": "An internal error occurs while processing."
}
```
## 503 Service Unavailable

This error is returned by the VASP(your) API server only in unexpected situations. In normal cases, you must respond with 200.

This is an error returned by VASP if an error occurred while the VASP API server processes a request. If the VASP API server returns an error, it passes through the CodeVASP API server to be sent to the VASP that has sent the request.

Status Code and ErrorType classification do not completely match CodeVASP API server.

* errorType: Error type
  * UNKNOWN\_TRANSFER\_ID: The requested TransferId does not exist.
  * VASP\_BACKEND\_INTERNAL\_ERROR: An internal error occurred while the VASP backend processes a request.
  * MISSING\_REQUIRED\_MSG\_FIELD: A required message field is missing.
  * MISSING\_HEADER\_FIELD: A required header field is missing.
  * INVALID\_MSG\_FIELD\_VALUE: The value of the message field is not valid.
  * DUPLICATED\_TRANSFER\_ID: This is a case where received TransferId already exists.
  * INVALID\_HEADER\_SIGNATURE: This is a case where the signature verification of the message header has failed.
  * INVALID\_ENCRYPTED\_BODY: This is a case an encrypted message cannot be decoded.
  * UNACCEPTABLE\_REQUEST: It is an unprocessable request. The content of the request cannot be processed by the VASP that received it.
* errorMsg: This is used to convey detailed information on the ErrorType. This is optional. 

```
{
  "errorType": "VASP_BACKEND_INTERNAL_ERROR",
  "errorMsg": "An VASP backend server failed to process request."
}
```