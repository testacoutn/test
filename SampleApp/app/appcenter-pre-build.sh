#!/usr/bin/env bash
# This updates the version number to be the build id from app center + the shift up to the build number from the google play store
# Currently we don't shift the ios version number.

PROJECT_NAME=test
ANDROID_GRADLE_FILE=$APPCENTER_SOURCE_DIRECTORY/test/SampleApp/app/gradle.properties
#INFO_PLIST_FILE=$APPCENTER_SOURCE_DIRECTORY/NameOfProjectOnAppCenter/ios/$PROJECT_NAME/Info.plist
VERSION_CODE=$((VERSION_CODE_SHIFT + APPCENTER_BUILD_ID))
echo "Looking for $ANDROID_GRADLE_FILE to update build version";

if [ -e "$ANDROID_GRADLE_FILE" ]
then
    echo "Updating version code to $VERSION_CODE in $ANDROID_GRADLE_FILE"
    sed -i "" 's/VERSION_CODE=[0-9]*/VERSION_CODE='$VERSION_CODE'/' $ANDROID_GRADLE_FILE

    echo "File content:"
    cat $ANDROID_GRADLE_FILE
fi
