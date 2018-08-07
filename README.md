# Person registry - minimal GUI application

The application handles a registry of people.</br>
This registry can come from a properly formatted file or can be created on the fly.</br>
The registry stored in the memory is not backed up, hence the user needs to save it to a file to make it permanent.

The registry stores a person's name, that identifies him/her.</br>
The registry may also store a person's phone number or email address if the one provided is considered valid.
Invalid data will be marked as "INVALID" in the output.

Any exception occurring during the running will be printed to the output with a leading "EXCEPTION:" marker.</br>
To inform the user of a decision or event, messages will be printed to the output with a leading "INFO:" marker.

## Open a registry

The user can open a registry file by providing an absolute path in the "File path" text field and pressing the "Process" button.</br>
The first non-empty line of a registry file should look something like "Name: Number, Email".
The separator characters may vary, but in case the structure is not recognised, the file won't be processed.

Any following non-empty line should represent a person.
If a transformation is possible, a Person object is created and stored in the registry.

For multiple reasons, the file processor logic is on a new thread that checks for interruption.

## Update or add a person

Three text fields are provided for entering custom data, one for each: the name, the phone number and the email address.</br>
Pressing the "Add/update person" triggers the modification.

A name has to be given, as registry uses the name to identify a person.</br>
If the person is found in the registry based on the name, his/her phone number and/or email address will be updated.
If any of these data is not provided or is identical with the stored value or considered invalid, the modification will not have any effect.

If the provided name is not found in the registry, a new person is added with the provided data.

## Delete a person

A person can be deleted from the registry by providing his/her name and pressing the "Delete person" button.</br>
If the provided name is not found in the registry, nothing happens.

## Save the registry

The user can save the registry to a file by providing an absolute path in the "File path" text field and pressing the "Save" button.</br>
The first line of the file will be the structured line (default is: "Name: Number, Email").

Any following non-empty line will represent a person in the registry existing in the memory.</br>
Any Person object stored in the registry will be transformed to a properly formed line according to the first line of the file.</br>
If a person has invalid data, an empty space-holder is printed. (Like: "Name: , Email".)

For multiple reasons, the saving logic is on a new thread that checks for interruption.

## Further characteristics
### Valid phone number
A phone number is considered valid if it has 11 digits and may start with a '+'.</br>
Every phone number is stored in the same format: "0620/123-4567" or "+3620/123-4567".

### Valid email address
An email address is considered valid if it passes apache-commons' EmailValidator's validation.</br>
