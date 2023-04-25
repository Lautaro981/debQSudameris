/**
 Compilador de traducciones
 **/

var fs = require('fs');

const TRANSLATION_FILE = 'conf/messages.es';
const END_ENUM_FILE = 'app/utils/translations/TranslationKey.java';
const CLASS_NAME = 'TranslationKey';
const PACKAGE = 'utils.translations';
const FILE_FORMAT = 'utf8';
const SPACE = ' ';
const DOT = '.';
const NL = '\n';


var fileStart = "package " + PACKAGE + ";" + NL + NL +
    "import com.debmedia.utils.controllers.translations.TranslatableKeys;" + NL + NL +
    "public enum " + CLASS_NAME + " implements TranslatableKeys {" + NL + NL;

var fileEnd = NL + "}";

compile();

function compile(){

    fs.readFile(TRANSLATION_FILE, FILE_FORMAT, function(err, data){
        //console.log(items);
        items = data.split(NL);
        fileData =fileStart;
        isFirst = true;
        for (var i=0; i<items.length; i++){
            if(items[i][0] == undefined || !items[i][0].match(/[a-zA-Z0-9_]/i)){
                continue;
            }
            enumValue = items[i].split("=")[0].trim();
            if(!enumValue.match("^[A-Z0-9_]*$")){
                continue;
            }
            if(isFirst){
                isFirst = false
            }else{
                fileData += ",\n";
            }
            fileData += enumValue;

        }

        fileData +=fileEnd;

        fs.writeFile(END_ENUM_FILE, fileData, function(err,data) {
            if (err) throw err;
            console.log('Se actualizo archivo de traducciones');
        });
    });
}