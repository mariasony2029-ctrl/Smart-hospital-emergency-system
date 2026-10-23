<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Smart Hospital Emergency Resource Management System (ERMS)</title>
    <!-- Fonts & Icons -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&family=JetBrains+Mono:wght@400;500;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    
    <style>
        :root {
            --bg-base: #0b1329;
            --bg-surface: #0f172a;
            --bg-card: #1e293b;
            --bg-card-hover: #273549;
            --border-color: #334155;
            --border-focus: #38bdf8;
            
            --text-main: #f8fafc;
            --text-muted: #94a3b8;
            --text-dim: #64748b;
            
            --accent-blue: #0284c7;
            --accent-blue-hover: #0369a1;
            --accent-blue-light: rgba(56, 189, 248, 0.1);
            
            --critical-red: #ef4444;
            --critical-bg: rgba(239, 68, 68, 0.15);
            --critical-border: rgba(239, 68, 68, 0.4);
            
            --urgent-amber: #f59e0b;
            --urgent-bg: rgba(245, 158, 11, 0.15);
            --urgent-border: rgba(245, 158, 11, 0.4);
            
            --stable-emerald: #10b981;
            --stable-bg: rgba(16, 185, 129, 0.15);
            --stable-border: rgba(16, 185, 129, 0.4);
            
            --purple-accent: #8b5cf6;
            --terminal-bg: #090d16;
            
            --radius-sm: 6px;
            --radius-md: 10px;
            --radius-lg: 16px;
            --shadow-card: 0 10px 25px -5px rgba(0, 0, 0, 0.4), 0 8px 10px -6px rgba(0, 0, 0, 0.3);
            --transition: all 0.25s cubic-bezier(0.4, 0, 0.2, 1);
        }

        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
        }

        body {
            font-family: 'Inter', -apple-system, BlinkMacSystemFont, sans-serif;
            background-color: var(--bg-base);
            color: var(--text-main);
            line-height: 1.5;
            padding: 0;
            margin: 0;
            min-height: 100vh;
            display: flex;
            flex-direction: column;
        }

        /* Scrollbar Styling */
        ::-webkit-scrollbar {
            width: 8px;
            height: 8px;
        }
        ::-webkit-scrollbar-track {
            background: var(--bg-base);
        }
        ::-webkit-scrollbar-thumb {
            background: var(--border-color);
            border-radius: 4px;
        }
        ::-webkit-scrollbar-thumb:hover {
            background: var(--text-dim);
        }

        /* Top Header Navigation */
        header {
            background: var(--bg-surface);
            border-bottom: 1px solid var(--border-color);
            padding: 1rem 2rem;
