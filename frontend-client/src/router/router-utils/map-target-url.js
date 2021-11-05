// map url to the redirected url
export default function MapUrl (url) {
    switch (url) {
        case "/":
            return "/layouts/client-home-welcome";
    
        default:
            // do nothing
            return url;
    }
}
  

