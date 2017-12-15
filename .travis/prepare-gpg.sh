#!/usr/bin/env bash
if ( [ "$TRAVIS_BRANCH" = 'master' ] || [ ! -z "$TRAVIS_TAG" ] ) && [ "$TRAVIS_PULL_REQUEST" == 'false' ]; then
	openssl aes-256-cbc -K $encrypted_9ffc84d5f72f_key -iv $encrypted_9ffc84d5f72f_iv -in codesigning.asc.enc -out codesigning.asc -d
	gpg --fast-import .travis/codesigning.asc
fi
