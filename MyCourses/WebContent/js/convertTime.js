/**
 * This is file that contain method that can convert PST to the local time.
 */

function convertTime(iID, oID) {
	var LA = moment.tz(document.getElementById(iID).value, "America/Los_Angeles");
	var PST = new Date(LA);
	
	document.getElementById(oID).value = PST.toLocaleString('en-GB', { timeZone: 'CST' });
	alert("Converting time");
}
