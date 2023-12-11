import React from 'react';
import Sidebar from '../components/Sidebar.js';
import CodeHome from '../components/CodeHome.js';
import QnAHome from '../components/QnAHome.js';


function HomePage() {
    return(
        <div style={{display:'flex'}}>
            <Sidebar />
           <QnAHome /> <CodeHome />
        </div>
        
    );
}

export default HomePage;