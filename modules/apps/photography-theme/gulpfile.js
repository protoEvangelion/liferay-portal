'use strict';

const gulp = require('gulp');
const liferayThemeTasks = require('liferay-theme-tasks');
const axios = require('axios')
const path = require('path')
const fs = require('fs')
const MsTranslator = require('mstranslator')

liferayThemeTasks.registerTasks({
	gulp: gulp,
	hookFn: function(gulp) {
		gulp.hook('before:deploy', function(done) {
			console.log('FIRST')
			gulp.start('add-text-to-language-properties-file')
			done()
		})
	}
});

gulp.task('add-text-to-language-properties-file', function() {
	
	const file = path.resolve(__dirname, './src/WEB-INF/src/content/Language.properties')
	
	fs.readFile(file, 'utf8', (err, contents) => {

		if (err) {
			console.log(err)

		} else {
			const propertiesArr = contents.split('\r\n')		
			let substringArr = []

			propertiesArr.forEach((string) => {
				substringArr.push(string.match(/=(.+)/)[1])
			})

			const url = 'https://api.cognitive.microsoft.com/sts/v1.0/issueToken?'
			const key = '39c6ec34dc684e9cae8c6be242041819'

			const client = new MsTranslator({ api_key: key }, true)
			
			const params = {
				texts: substringArr,
				from: 'en',
				to: 'es'
			}
			
			// Don't worry about access token, it will be auto-generated if needed. 
			client.translateArray(params, function(err, data) {

				if (err) {
					console.log(err)

				} else {
					const translated = data.map((translation, i) => {

						const string = propertiesArr[i]

						return string.slice(0, string.indexOf('=') + 1) + translation.TranslatedText
					
					})

					const esFile = path.resolve(__dirname, './src/WEB-INF/src/content/Language_es.properties')

					fs.writeFile(esFile, translated.join('\r\n'), (err) => {
						err ? console.log(err) : console.log('it worked')
					})
				}
			})
		}
	})
})