# RiseHackathon
Rise 36 hour hackathon

This a simple yet disruptive/highly automated android/web application for automating the entire KYC experience for banking customer.

The application is built on 2 important models
1. Easing the customer workflow for opening a new account
2. Adhering to govt guidelines  for opening a bank account.

Vision1 :In the midst of night,if the customer feels to open the bank account,she will use the authentica app and in 5 touch  she knows she has selected the right bank.
WorkFlow:
User opens the app.
She fills in the basic details like name,address,age,etc required for KYC account opening.
She also fills in the username and password details for the mobile wallet.

After filling these details, the user would be given option for selecting the KYC document.
If the user selects Adhar as her preferred way of KYC document,then the next screen will prompt for Adhar number.Along with adhar number,a biometric thumb scanner will be used to collect the biometric details of the user.(Wondering if this is an external device? No this will be using the feature of fingerprint scanner. Apple has this inbuilt device since September 20, 2013. New android smartphones have started supporting this feature. And it would be just a matter of time before it becomes a part of mainstream.remember front cameras 3 years back!)

The details will be hashed and sent to UDAI server. More details on low level working: https://authportal.uidai.gov.in/static/ekyc_policy_note_18122012.pdf

The verification will require an OTP which will be sent on customer's mobile.
On entering the right OTP, the customer would be recieving the digitally signed certificate.
And whoa! the customer gets his bank account opened and also the facility to use his walet(no more limit of 10,000 INR)

The app also supports for the customers not having Adhar id.They simply need to upload the relevant documents.
A selfie to verify themselves. But this will involve a manual background check to verify the customer's original document.(ImageProcessing to compare the uploaded documents and photographs needs to be researched).
Yes but at the end it's the customer who lives the King's way. No hassles,No pain.

