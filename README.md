# YGOTAS Companion
An Android application that displays Yu-Gi-Oh The Abridged Series (and other Yu-Gi-Oh content) quotes and sound clips, allowing the user to interact and play them from a single app. Also contains features displaying custom house rules, aids for a duel in progress and other Yu-Gi-Oh related features. Intended for personal use.

*Note: This application was developed almost entirely using Jetpack Compose, and is also the first Jetpack Compose application created by a Firmware Engineer who is mostly unfamiliar with modern app development practices. If there is strange code smells or behavior, it is likely unintended and a side effect of an engineer stepping out of his comfort zone.*

## House Rules
House rules can be imported from the **House Rules** or **Settings** pages. The imported House Rules file accepts any standard Markdown file (like this one). 

A sample one, the rules at the time of this app's creation, exist within the *documentation/house_rules* folder.

## Quotes
The primary purpose of this application is to allow for the playing of YGOTAS (Yu-Gi-Oh The Abridged Series) quotes during Yu-Gi-Oh duels. As the number of quotes is fairly large, and is ever-changing as more are added, quotes are not automatically included as part of the standard application package.

The following section covers how to format audio files for usage.

### Quote File Format, Tags
Quotes can be imported from the **Quotes** or **Settings** pages.

This application has been tested to use **.wav** files, and no other file formats.

Quotes should be imported as a zipped folder, with only files at the root level. The app may reject the contents if the zipped file contains any nested folders.

Every audio file that is imported as part of this must have custom metadata as part of the file - as this is used by the application to help identify the sound quotes and various information associated with them. Not including any metadata may cause the app to reject the files.
It is recommended to use a free program like Mp3Tag (https://www.mp3tag.de/en/) to modify a sound file's metadata.

It is also recommended to start from a fresh slate - with no metadata. This can be done by selecting the folder (or files) and selecting the **Remove Tag** option within Mp3Tag.

To modify a file's metadata, select the file in Mp3Tag and press **Alt + T**. From there custom fields can be entered by selecting the **Add field** button.

The following sections cover each tag. Unless otherwise specfied, the name of the field should match the header. All fields are optional, but can be used to help aid in finding a quote.

#### EPISODE_NUMBER
Contains the episode number from which the quote originates.

Format:
* N

#### EPISODE_TITLE
Contains the title of the episode from which the quote originates.

Format:
* Title of the Episode

#### QUOTE_TEXT
Contains the text of the quote.

Format:
* I am the text of the quote.

#### TAGS
Contains one (or more) tags by which to identify the quotes. Note: Regarding tags, make sure the spelling is consistent - as having multiple similarly-named tags will each show up as separate tags in the system.

No need for spaces between the delimiters and leading and trailing spaces are removed automatically.

Format:
* Funny
* Funny,Happy,Joey

## Links
Note: Full credit to the Kuriboh icons goes to Maxiuchiha22 on DeviantArt
- https://www.deviantart.com/maxiuchiha22/art/Kuriboh-render-4-Monster-Strike-855820171
- https://www.deviantart.com/maxiuchiha22/art/Kuriboh-render-3-Monster-Strike-855820164
- https://www.deviantart.com/maxiuchiha22/art/Kuriboh-render-Puzzle-and-Dragons-851290383