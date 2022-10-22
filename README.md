# JsonBasedDynamicUi_kotlin
Android Json Wizard is a library for creating beautiful dynamic UI wizards with pure kotlin within your app just by defining json in a particular format.

Demo
JsonBasedDynamicUi_kotlin_demo


Usage
Json Structure
Form json should consist of steps and fields.

Steps
Step directly corresponds to a fragment(or a page) in wizard. It consists of different fields(array of fields), title and next step.

    {
     "step1":{
             "fields":[
                 {
                     "key":"name",
                     "type":"edit_text",
                     "hint":"Enter Your Name"
                 },
                 {
                     "key":"email",
                     "type":"edit_text",
                     "hint":"Enter email address"
                 },
                 {
                     "key":"labelBackgroundImage",
                     "type":"label",
                     "text":"Choose Background Image"
                 },
                 {
                     "key":"chooseImage",
                     "type":"choose_image",
                     "uploadButtonText":"Choose"
                 },
                 {
                     "key":"getAddressFromLatlng",
                     "type":"gps",
                     "options":[
                        {
                          "val":"2.334,3.444"
                        }
                     ]
                 },
                 {
                     "key":"showDropDown",
                     "type":"spinner",
                     "options":[
                        {
                         "default":true,
                         "displayText":"Delhi"
                        },
                         {
                         "default":false,
                         "displayText":"Mumbai"
                        }
                     ]
                 }
             ],
             "title":"Step 1",
             "next":"step2"
         }
    }
    
    
key - It must be unique in that particular step.

type - It must be edit_text for EditText.

hint - hint for EditText.

value - will be the value present in the editText after completion of wizard

key - must be unique in that particular step.

type - must be label for Label.

text - text for Label.



Add library for own application
gradle:

dependencies {
    implementation lib("dynamicui")
}


TODOs
Support validation for Checkbox and RadioButton.
Improve recylerview adapter as list


License
This project is licensed under the MIT License. Please refer the License.txt file.
